package org.zerock.domain;
 
import java.util.Arrays;
import java.util.Date;
/***
 * create table tbl_board
(
	BNO	int not null auto_increment
    ,TITLE	varchar(200) not null
    ,CONTENT text null
    ,WRITER	varchar(50) not null
    ,REGDATE	timestamp not null	default	now()
    #,UPDATEDATE	timestamp	default	now()
    ,VIEW_COUNT	int default 0
    ,primary key(BNO)
);
 */
public class BoardVO {

	private Integer bno;
	private String title;
	private String content;
	private String writer;
	private Date regdate;
	private int viewcnt;
	private int replycnt;
	private String[] files; //...586p.Ã·ºÎÆÄÀÏµé...
	
	
	public Integer getBno() {
		return bno;
	}
	public void setBno(Integer bno) {
		this.bno = bno;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	public int getViewcnt() {
		return viewcnt;
	}
	public void setViewcnt(int viewcnt) {
		this.viewcnt = viewcnt;
	}
	
	public int getReplycnt() {
		return replycnt;
	}

	public void setReplycnt(int replycnt) {
		this.replycnt = replycnt;
	}

	public String[] getFiles() {
		return files;
	}

	public void setFiles(String[] files) {
		this.files = files;
	}

  
	@Override
	public String toString() {
		return "BoardVO [bno=" + bno + ", title=" + title 
				+ ", content=" + content + ", writer=" + writer 
				+ ", regdate=" + regdate + ", viewcnt=" + viewcnt  
				+ ", replycnt=" + replycnt 
				   + ", files=" + Arrays.toString(files) + "]";
	}
		
	
}