package jadx.plugins.tfoc;

import jadx.api.JadxArgs;
import jadx.api.JadxDecompiler;
import jadx.api.JavaClass;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JadxNameFigurerOuterTest {

	@Test
	public void jsonComplexTest() throws Exception {
		JadxArgs args = new JadxArgs();
		args.getInputFiles().add(getSampleFile("bar.smali"));
		args.getInputFiles().add(getSampleFile("baz.smali"));
		args.getInputFiles().add(getSampleFile("foo.smali"));

		try (JadxDecompiler jadx = new JadxDecompiler(args)) {
			jadx.load();

			List<JavaClass> classes = jadx.getClasses();
			assertThat(classes).isNotNull();

			for (JavaClass cls : classes) {

				// TODO: Why does this not work in here, but does work in the GUI??
				// if (cls.getFullName().equals("com.tfoc.hello.Bar")) {
				// assertThat(cls.getFields().get(0).getName()).isEqualTo("secret");
				// assertThat(cls.getFields().get(1).getName()).isEqualTo("device_name");
				// assertThat(cls.getFields().get(2).getName()).isEqualTo("age");
				// assertThat(cls.getFields().get(3).getName()).isEqualTo("epoch_nanos");
				// assertThat(cls.getFields().get(4).getName()).isEqualTo("pi");
				// assertThat(cls.getFields().get(5).getName()).isEqualTo("evil");
				// }

				if (cls.getFullName().equals("com.tfoc.hello.Baz")) {
					assertThat(cls.getCode()).contains("String device_name = json.getString(\"device_name\");");
				}

				if (cls.getFullName().equals("com.tfoc.hello.Foo")) {
					assertThat(cls.getFields().get(0).getName()).isEqualTo("secret");
					assertThat(cls.getFields().get(1).getName()).isEqualTo("device_name");
					assertThat(cls.getFields().get(2).getName()).isEqualTo("age");
					assertThat(cls.getFields().get(3).getName()).isEqualTo("epoch_nanos");
					assertThat(cls.getFields().get(4).getName()).isEqualTo("pi");
					assertThat(cls.getFields().get(5).getName()).isEqualTo("evil");
				}

				System.out.println(cls.getCode());
			}
		}
	}

	@Test
	public void jsonOptTest() throws Exception {
		JadxArgs args = new JadxArgs();
		args.getInputFiles().add(getSampleFile("optFoo.smali"));

		try (JadxDecompiler jadx = new JadxDecompiler(args)) {
			jadx.load();

			List<JavaClass> classes = jadx.getClasses();
			assertThat(classes).isNotNull();

			for (JavaClass cls : classes) {

				if (cls.getFullName().equals("com.tfoc.hello.OptFoo")) {
					assertThat(cls.getFields().get(0).getName()).isEqualTo("secret");
					assertThat(cls.getFields().get(1).getName()).isEqualTo("device_name");
					assertThat(cls.getFields().get(2).getName()).isEqualTo("age");
					assertThat(cls.getFields().get(3).getName()).isEqualTo("missing_int");
					assertThat(cls.getFields().get(4).getName()).isEqualTo("epoch_nanos");
					assertThat(cls.getFields().get(5).getName()).isEqualTo("missing_long");
					assertThat(cls.getFields().get(6).getName()).isEqualTo("pi");
					assertThat(cls.getFields().get(7).getName()).isEqualTo("missing_double");
					assertThat(cls.getFields().get(8).getName()).isEqualTo("evil");
					assertThat(cls.getFields().get(9).getName()).isEqualTo("missing_boolean");

					assertThat(cls.getCode()).contains("String missing_string = json.optString(\"missing_string\");");
				}

				System.out.println(cls.getCode());
			}
		}
	}

	private File getSampleFile(String fileName) throws URISyntaxException {
		URL file = getClass().getClassLoader().getResource("samples/" + fileName);
		assertThat(file).isNotNull();
		return new File(file.toURI());
	}
}
