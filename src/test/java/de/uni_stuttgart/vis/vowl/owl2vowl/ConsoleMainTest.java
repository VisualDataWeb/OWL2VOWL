package de.uni_stuttgart.vis.vowl.owl2vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.converter.AbstractConverter;
import de.uni_stuttgart.vis.vowl.owl2vowl.converter.IRIConverter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.FileExporter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.semanticweb.owlapi.model.IRI;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AbstractConverter.class)
@PowerMockIgnore( {"javax.management.*"})
public class ConsoleMainTest extends ConsoleMain {

	private static final String TEST_PATH = "testPfad";
	private static final String TEST_IRI = "http://testiri.de";

	@Test
	public void testMain() throws Exception {
		IRIConverter mockedConverter = mock(IRIConverter.class);
		doNothing().when(mockedConverter).convert();

		PowerMockito.whenNew(IRIConverter.class).withArguments(Mockito.any(IRI.class), anyCollection()).thenReturn(mockedConverter);

		ConsoleMain.main(new String[]{"-iri", "http://test.de"});
	}

	@Test
	public void testOutputParameter() throws Exception {
		IRIConverter mockedConverter = mock(IRIConverter.class);
		doNothing().when(mockedConverter).convert();
		PowerMockito.whenNew(IRIConverter.class).withArguments(Mockito.any(IRI.class), anyCollection()).thenReturn(mockedConverter);

		FileExporter mockedFileExporter = mock(FileExporter.class);
		PowerMockito.whenNew(FileExporter.class).withArguments(Mockito.any(File.class)).thenAnswer(invocationOnMock -> {
			File file = (File) invocationOnMock.getArguments()[0];
			assertThat(file.getName(), is(TEST_PATH));
			return mockedFileExporter;
		});

		new ConsoleMain().parseCommandLine(new String[]{"-output", TEST_PATH, "-iri", TEST_IRI});
	}
}
