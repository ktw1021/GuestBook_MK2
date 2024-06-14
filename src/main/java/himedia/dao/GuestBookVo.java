package himedia.dao;

public class GuestBookVo {
	private Long no;
	private String name;
	private String password;
	private String reg_date;
	private String content;
	
	
	public GuestBookVo() {
		super();
	}
	public GuestBookVo(Long no, String name, String password, String reg_date, String content) {
		super();
		this.no = no;
		this.name = name;
		this.password = password;
		this.reg_date = reg_date;
		this.content = content;
	}
	
	
	public GuestBookVo(Long no, String name, String reg_date, String content) {
		super();
		this.no = no;
		this.name = name;
		this.reg_date = reg_date;
		this.content = content;
	}
	
	
	public GuestBookVo(String name, String password, String content) {
		super();
		this.name = name;
		this.password = password;
		this.content = content;
	}
	
	
	public GuestBookVo(Long no, String name, String content) {
		super();
		this.no = no;
		this.name = name;
		this.content = content;
	}
	/**
	 * @return the no
	 */
	public Long getNo() {
		return no;
	}
	/**
	 * @param no the no to set
	 */
	public void setNo(Long no) {
		this.no = no;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the reg_date
	 */
	public String getReg_date() {
		return reg_date;
	}
	/**
	 * @param reg_date the reg_date to set
	 */
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "GuestBookVo [no=" + no + ", name=" + name + ", password=" + password + ", reg_date=" + reg_date
				+ ", content=" + content + "]";
	}
	
	
}
