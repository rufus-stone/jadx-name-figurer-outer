package jadx.plugins.tfoc;

import jadx.api.JadxArgs;
import jadx.api.JadxDecompiler;
import jadx.api.JavaClass;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

class JadxNameFigurerOuterTest {

	@Test
	public void jsonSimpleTest() throws Exception {
		JadxArgs args = new JadxArgs();
		args.getInputFiles().add(getSampleFile("jsonSimple.smali"));
		// args.getInputFiles().add(getSampleFile("hello2.smali"));
		try (JadxDecompiler jadx = new JadxDecompiler(args)) {
			jadx.load();

			JavaClass cls = jadx.getClasses().get(0);

			String clsCode = cls.getCode();
			System.out.println(clsCode);

			assertThat(cls.getFields().get(0).getName()).isEqualTo("foo");
			assertThat(cls.getFields().get(1).getName()).isEqualTo("bar");
			assertThat(cls.getFields().get(2).getName()).isEqualTo("baz");
		}
	}

	private File getSampleFile(String fileName) throws URISyntaxException {
		URL file = getClass().getClassLoader().getResource("samples/" + fileName);
		assertThat(file).isNotNull();
		return new File(file.toURI());
	}
}
