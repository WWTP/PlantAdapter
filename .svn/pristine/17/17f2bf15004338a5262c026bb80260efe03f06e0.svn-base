package tests.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import plantadapter.results.Result;

public class ResultWriter implements Writer<Result>{

	public static int DEFAULT_SPLIT_TIME = 1000 * 60;
	
	private int records = 0;
	private String path;
	private String nameTemplate;
	
	private int splitTime;
	private Date lastWrite = new Date(0);
	
	private PrintWriter pw;
	
	private boolean closed = false;
	
	@Override
	public void write(Result item) throws IOException, IllegalStateException {
		if(closed)
			throw new IllegalStateException("Writer closed");
		
		if(item.getTimestamp().getTime() - lastWrite.getTime() > splitTime)
		{
			if(pw != null)
				pw.close();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			
			this.lastWrite = item.getTimestamp();
			pw = new PrintWriter(
				new FileWriter(
					this.path + sdf.format(this.lastWrite) + this.nameTemplate
				)
			);
		}
		
		pw.println(item);
		records++;
	}

	@Override
	public void close() {
		if(pw != null)
			pw.close();
		closed = true;
	}
	
}
