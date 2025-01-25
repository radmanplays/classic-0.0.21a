package net.PeytonPlayz585.io;

import java.util.Collection;
import java.util.List;

import net.PeytonPlayz585.opengl.LWJGLMain;

public class File {
	
	private String path;
	
	public File(String path) {
		this.path = path;
	}
	
	public File(String parent, String child) {
		this.path = parent + "/" + child;
	}
	
	public File(File parent, String child) {
		this.path = parent.getPath() + "/" + child;
	}
	
	public String getPath() {
		return path;
	}
	
	public boolean exists() {
		if(LWJGLMain.directoryExists(path) || LWJGLMain.fileExists(path)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean mkdirs() {
	    String[] directories = path.split("/");
	    String currentPath = "";

	    for (String directory : directories) {
	        currentPath += directory + "/";
	        if (!LWJGLMain.directoryExists(currentPath) && !LWJGLMain.pathExists(currentPath)) {
	            LWJGLMain.writeFile(currentPath, new byte[0]);
	            if(LWJGLMain.readFile(currentPath) == null) {
	            	return false;
	            }
	        }
	    }

	    return true;
	}
	
	public boolean mkdir() {
	    if (!LWJGLMain.listFiles(path, false, false).isEmpty()) {
	        return false;
	    }

	    LWJGLMain.writeFile(path, new byte[0]);
	    if(LWJGLMain.readFile(path) != null) {
	    	return true;
	    } else {
	    	return false;
	    }
	}
	
	public byte[] getBytes() {
		return LWJGLMain.readFile(path);
	}
	
	public void writeBytes(byte[] data) {
		LWJGLMain.writeFile(path, data);
	}
	
	public long length() {
		return LWJGLMain.getFileSize(path);
	}

	public void delete() {
		LWJGLMain.writeFile(path, null);
	}
	
	public File[] listFiles() {
	    Collection<LWJGLMain.FileEntry> fileEntries = LWJGLMain.listFiles(path, false, false);
	    File[] files = new File[fileEntries.size()];
	    int i = 0;
	    for (LWJGLMain.FileEntry fileEntry : fileEntries) {
	        files[i++] = new File(fileEntry.path);
	    }
	    return files;
	}

	public boolean isDirectory() {
		return LWJGLMain.directoryExists(path);
	}

	public void renameTo(File var4) {
		LWJGLMain.renameFile(path, var4.getPath());
	}
}
