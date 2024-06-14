package himedia.dao;

import java.util.List;
import java.util.Map;

public interface GuestBookDao {
	public List<GuestBookVo> getList();
	public boolean insert(String name, String password, String content);
	public boolean deleteWithPasswordCheck(long no, String password);
	public Map<String, String> getEntry(int no);
	public boolean updateEntry(int no, String name, String password, String content);
}
