package br.com.wanclaybarreto.paineltarefas.util;

public enum FileType {
	
	PNG("image/png", "png"),
	JPG("image/jpeg", "jpg"),
	MP4("video/mp4", "mp4"),
	MOV("video/quicktime", "mov"),
	OGG("video/ogg", "ogg");
	
	String mimeType;
	String extension;
	
	FileType(String mimeType, String extension) {
		this.mimeType = mimeType;
		this.extension = extension;
	}
	
	public boolean sameOf(String mimeType) {
		return this.mimeType.equalsIgnoreCase(mimeType);
	}
	
	public static FileType of(String mimeType) {
		for (FileType fileType : values()) {
			if (fileType.sameOf(mimeType)) {
				return fileType;
			}
		}
		
		return null;
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getExtension() {
		return extension;
	}
	
}
