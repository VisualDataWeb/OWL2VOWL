package de.uni_stuttgart.vis.vowl.owl2vowl.server;

import de.uni_stuttgart.vis.vowl.owl2vowl.Owl2Vowl;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.LoggingConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.IOUtils;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.net.URL;
import java.text.NumberFormat;





@RestController
public class Owl2VowlController {

	private static final Logger logger = LogManager.getLogger(Owl2VowlController.class);

	private static final Logger conversionLogger = LogManager.getLogger(LoggingConstants.CONVERSION_LOGGER);
	private static final String СONVERT_MAPPING = "/convert";
	private static final String READ_JSON = "/read";
	
	private static final String LOADING_STATUS = "/loadingStatus";
	private static final String SERVER_TIMESTAMP = "/serverTimeStamp";
	private static final String CONVERSION_DONE = "/conversionDone";
	private static final String DIRECT_INPUT = "/directInput";
	private static final String GIZMO_INPUT = "/gizMOInput";
	
	public  static       String loadingStatusMsg="";
	private static Boolean needConversionID=true;
	
	Map<String, Owl2Vowl> conversionSessionMap=new HashMap<String, Owl2Vowl>();
	
	public String msgForWebVOWL(String stackTrace) {
		// converts the < and > tags to html string so no html injection is created
		String s1= stackTrace.replaceAll("<", "&lt;");
		String s2= s1.replaceAll(">", "&gt;");
		return s2;
	}
	
	public void timed_provideMemoryReleaseHintForGC(String sessionId, String msg, Timer t ) {
		t.cancel();
		t.purge();
		t=null;
		provideMemoryReleaseHintForGC(sessionId, msg);
	}
	
	public void provideMemoryReleaseHintForGC(String sessionId, String msg) {
		Owl2Vowl conversionInstace= conversionSessionMap.get(sessionId);
		if (conversionInstace!=null) {
			Runtime runtime = Runtime.getRuntime();
			NumberFormat format = NumberFormat.getInstance();
			long allocatedMemory = runtime.totalMemory();
			long freeMemory = runtime.freeMemory();

			conversionSessionMap.remove(sessionId);
			conversionInstace=null;
			System.gc();
			System.runFinalization();
			System.out.println("Cleaning up Memory for session Id "+ sessionId+ " << closed Session ");
			System.out.println("Conversion Finished "+msg+" ");
		
			// create some statistics
			format = NumberFormat.getInstance();
			long after_allocatedMemory = runtime.totalMemory();
			long after_freeMemory = runtime.freeMemory();
			System.out.println("-> USED MEMORY " + format.format((allocatedMemory - freeMemory) / 1024)      + "  ->    "+format.format( (after_allocatedMemory- after_freeMemory) / 1024)+"  ");
		    
		}
	}
	
	synchronized public void evaluateLifeCycleOfConversionObject(String sessionId, long numOfMiliSeconds) { 
		Owl2Vowl conversionInstace= conversionSessionMap.get(sessionId);
		if (conversionInstace!=null) {
			 final Timer timer = new Timer();
			    timer.schedule(new TimerTask() {
		        @Override
			        public void run() {
		        	timed_provideMemoryReleaseHintForGC(sessionId, " -> Live cycle evaluation request", timer);
		        	}
			    },numOfMiliSeconds,numOfMiliSeconds);
		}
   }
	
	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Parameter not correct")
	@ExceptionHandler(IllegalArgumentException.class)
	public void parameterExceptionHandler(Exception e) {
		logger.info("--- Parameter exception: " + e.getMessage());
		conversionLogger.error("Problems with parameters: " + e.getMessage());
		loadingStatusMsg+="* <span style='color:red;'>Problems with parameters:</span>\n";
		loadingStatusMsg+=msgForWebVOWL(e.getMessage());
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Ontology could not be created")
	@ExceptionHandler(OWLOntologyCreationException.class)
	public void ontologyCreationExceptionHandler(Exception e) {
		logger.info("--- Ontology creation exception: " + e.getMessage());
		conversionLogger.error("Problems in ontology creation process: " + e.getMessage());
		loadingStatusMsg+="* <span style='color:red;'>Problems in ontology creation process:</span>\n";
		loadingStatusMsg+=msgForWebVOWL(e.getMessage());
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Problems while generating uploaded file on server.")
	@ExceptionHandler(IOException.class)
	public void fileExceptionHandler(Exception e) {
		logger.info("--- IO exception: " + e.getMessage());
		conversionLogger.error("IO exception while generating file on server: " + e.getMessage());
		loadingStatusMsg+="* <span style='color:red;'>IO exception while generating file on server:</span>\n";
		loadingStatusMsg+=msgForWebVOWL(e.getMessage());
	}

	// adding the conversion loading to webvowl;
	@RequestMapping(value = LOADING_STATUS, method = RequestMethod.GET)
	public String getLoadingStatus(@RequestParam("sessionId") String sessionId){
		Owl2Vowl converstionInstace= conversionSessionMap.get(sessionId);
		if (converstionInstace!=null) {
			// add a . for loading indication;
			converstionInstace.appendLoadingIndicatorPoint();
			return converstionInstace.getLoadingMsg();
		}
		else {
			if (needConversionID==true) {
				loadingStatusMsg+=".";
				return loadingStatusMsg;
			}else {
				return loadingStatusMsg;
			}
		}
	}
	
	// adding finished call from client, in order to remove owl2vowl converter from map and 
	// cleanup memory by garbage collector the wise 
	@RequestMapping(value = CONVERSION_DONE, method = RequestMethod.GET)
	public void conversionFinished(@RequestParam("sessionId") String sessionId){
		provideMemoryReleaseHintForGC(sessionId,"-> Requested by WebVOWL");
	}
	
	// adding session id to owl2vowl;
	@RequestMapping(value = SERVER_TIMESTAMP, method = RequestMethod.GET)
		public String getServerTimeStamp(){
		  needConversionID=true;
	  	  Date currentTime=new Date();
    	  long SystemTimeStamp=currentTime.getTime();
    	  
    	  String value=String.valueOf(SystemTimeStamp);
    	  loadingStatusMsg="";
    	  System.out.println("Requested session Id:             "+ value);
   		  return value;
		}
	
	@RequestMapping(value = DIRECT_INPUT, method = RequestMethod.POST)
	public String directInput(@RequestParam("input")String input, @RequestParam("sessionId") String sessionId) throws IOException,
			OWLOntologyCreationException {
		String jsonAsString;
		try {
			Owl2Vowl owl2Vowl = new Owl2Vowl(input);
			conversionSessionMap.put(sessionId, owl2Vowl);
			jsonAsString = owl2Vowl.getJsonAsString();
		} catch (Exception e) {
			conversionLogger.info("Error for direct Input  " + e.getMessage());
			throw e;
		}
		finally {
			evaluateLifeCycleOfConversionObject(sessionId,5000);
		}
	
		return jsonAsString;
	}
	
	
	@RequestMapping(value = СONVERT_MAPPING, method = RequestMethod.POST)
	public String uploadOntology(@RequestParam("ontology") MultipartFile[] files, @RequestParam("sessionId") String sessionId) throws IOException,
			OWLOntologyCreationException {

		if (files == null || files.length == 0) {
			loadingStatusMsg+="* <span style='color:red;'>No file uploaded!</span>";
			throw new IllegalArgumentException("No file uploaded!");
		}

		if (files.length > 1) {
			loadingStatusMsg+="* <span style='color:red;'>Please upload only the main ontology!</span>";
			throw new IllegalArgumentException("Please upload only the main ontology!");
		}

		List<InputStream> inputStreams = new ArrayList<>();

		for (MultipartFile file : files) {
			inputStreams.add(file.getInputStream());
		}

		String jsonAsString;

		try {
			Owl2Vowl owl2Vowl = new Owl2Vowl(inputStreams.get(0));
			conversionSessionMap.put(sessionId, owl2Vowl);
			jsonAsString = owl2Vowl.getJsonAsString();
		} catch (Exception e) {
			conversionLogger.info("Uploaded files " + files[0].getName() + ": " + 0);
			throw e;
		}
		finally {
			evaluateLifeCycleOfConversionObject(sessionId,5000);
		}
		return jsonAsString;
	}
	
	@RequestMapping(value = GIZMO_INPUT, method = RequestMethod.POST)
	public String uploadOntology(@RequestParam("ontology") MultipartFile[] files) throws IOException,
			OWLOntologyCreationException {
		if (files == null || files.length == 0) {
			loadingStatusMsg+="* <span style='color:red;'>No file uploaded!</span>";
			throw new IllegalArgumentException("No file uploaded!");
		}

		if (files.length > 1) {
			loadingStatusMsg+="* <span style='color:red;'>Please upload only the main ontology!</span>";
			throw new IllegalArgumentException("Please upload only the main ontology!");
		}

		List<InputStream> inputStreams = new ArrayList<>();

		for (MultipartFile file : files) {
			inputStreams.add(file.getInputStream());
		}

		String jsonAsString;

		try {
			Owl2Vowl owl2Vowl = new Owl2Vowl(inputStreams.get(0));
			//conversionSessionMap.put(sessionId, owl2Vowl);
			jsonAsString = owl2Vowl.getJsonAsString();
		} catch (Exception e) {
			conversionLogger.info("Uploaded files " + files[0].getName() + ": " + 0);
			throw e;
		}
		finally {
			//evaluateLifeCycleOfConversionObject(sessionId,5000);
		}
		return jsonAsString;
	}


	@RequestMapping(value = СONVERT_MAPPING, method = RequestMethod.GET)
	public String convertIRI(@RequestParam("iri") String iri,@RequestParam("sessionId") String sessionId) throws IOException, OWLOntologyCreationException {
		String jsonAsString;
		String out=iri.replace(" ","%20");
		loadingStatusMsg="";
		try {
		
			Owl2Vowl owl2Vowl = new Owl2Vowl(IRI.create(out));
			conversionSessionMap.put(sessionId, owl2Vowl);
   		    jsonAsString =  owl2Vowl.getJsonAsString();
		
			conversionLogger.info(out + " " + 1);
		} catch (Exception e) {
			conversionLogger.info(out + " " + 1);
			return e.getMessage();
		}
		finally {
			evaluateLifeCycleOfConversionObject(sessionId,5000);
		}
		conversionLogger.info(iri + " " + 0);
		return jsonAsString;
	}

	// adding some proxy functionality

	@RequestMapping(value = READ_JSON, method = RequestMethod.POST)
	public String uploadJSON(@RequestParam("ontology") MultipartFile[] files) throws IOException,
			OWLOntologyCreationException {
		// clear loading String; 
		if (files == null || files.length == 0) {
			loadingStatusMsg+="No file uploaded!";
			throw new IllegalArgumentException("No file uploaded!");
		}

		if (files.length > 1) {
			loadingStatusMsg+="* <span style='color:red;'>Please upload only the main ontology!</span>";
			throw new IllegalArgumentException("Please upload only the main ontology!");
		}

		List<InputStream> inputStreams = new ArrayList<>();

		String jsonAsString="";
		for (MultipartFile file : files) {
			inputStreams.add(file.getInputStream());
		}

		try {
			jsonAsString=IOUtils.toString(inputStreams.get(0),"UTF-8");
		}
		catch (Exception e){
			logger.info("Something went wrong");
			conversionLogger.error("Something went wrong " + e.getMessage());
			loadingStatusMsg+="* <span style='color:red;'> Something went wrong</span>\n";
			loadingStatusMsg+=msgForWebVOWL(e.getMessage());
		}

		return jsonAsString;
	}

	
	
	@RequestMapping(value = READ_JSON, method = RequestMethod.GET)
	public String readJsons(@RequestParam("json") String json) throws IOException, OWLOntologyCreationException {
		String jsonAsString="";
		needConversionID=false;
		try {
			InputStream input = new URL(json).openStream();
			try {
				jsonAsString=IOUtils.toString(input,"UTF-8");
			} catch (Exception e2){
				loadingStatusMsg+="* <span style='color:red;'> Something went wrong</span>\n";
				loadingStatusMsg+=msgForWebVOWL(e2.getMessage());
			}finally {
				IOUtils.closeQuietly(input);
			}
		}
		catch (Exception e){
			logger.info("Something went wrong");
			conversionLogger.error("Something went wrong " + e.getMessage());
//			loadingStatusMsg+="* <span style='color:red;'> Something went wrong</span>\n";
//			loadingStatusMsg+=msgForWebVOWL(e.getMessage());
			conversionLogger.error(loadingStatusMsg);
		}
		return jsonAsString;
	}

}
