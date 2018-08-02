package org.zerock.persistence;
 
import java.util.List;

import org.zerock.domain.BoardVO;
  
public interface BoardDAO {

	  public void regist(BoardVO vo) throws Exception;
	  public BoardVO read(Integer bno) throws Exception;
	  public void update(BoardVO vo) throws Exception;
	  public void remove(Integer bno) throws Exception;
	  public List<BoardVO> listAll() throws Exception;	
	
}