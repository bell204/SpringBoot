package org.zerock.persistence;
 
import java.util.List;

import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.SearchCriteria;
  
public interface BoardDAO {

	  public void regist(BoardVO vo) throws Exception;
	  public BoardVO read(Integer bno) throws Exception;
	  public void update(BoardVO vo) throws Exception;
	  public void remove(Integer bno) throws Exception;
	  public List<BoardVO> listAll() throws Exception;	
	  public List<BoardVO> listPage(int page) throws Exception;	
	  public List<BoardVO> listCriteria(Criteria cri) throws Exception;
	  public int countPaging(Criteria cri) throws Exception;
	  //public int CountPaging(Criteria cri);
	  public List<BoardVO> listSearch(SearchCriteria cri) throws Exception;
	  public int listSearchCount(SearchCriteria cri) throws Exception;
	  
	  
	  public void updateReplyCnt(Integer bno, int amount)throws Exception;
	  public void updateViewCnt(Integer bno)throws Exception;
	  
	  public void addAttach(String fullName) throws Exception;
	  public List<String> getAttach(Integer bno)throws Exception; 
	  public void deleteAttach(Integer bno)throws Exception;
	  public void replaceAttach(String fullName, Integer bno)throws Exception;
	  
}

 