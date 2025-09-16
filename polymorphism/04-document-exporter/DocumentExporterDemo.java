public class DocumentExporterDemo {
	public static void main(String[] args) {
		DocumentExporter[] exporters = new DocumentExporter[] {
			new PdfExporter(),
			new WordExporter(),
			new HtmlExporter()
		};

		String content = "Hello Polymorphism";
		for (DocumentExporter exporter : exporters) {
			String out = exporter.export(content);
			System.out.println(exporter.getName() + " => " + out);
		}
	}
}


