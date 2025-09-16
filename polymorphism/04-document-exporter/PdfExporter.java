public class PdfExporter implements DocumentExporter {
	@Override
	public String export(String content) {
		return "%PDF-1.7\n" + content + "\n%%EOF";
	}
}


