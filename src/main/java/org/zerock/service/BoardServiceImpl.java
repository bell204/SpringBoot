package org.zerock.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.SearchCriteria;
import org.zerock.persistence.BoardDAO;

@Service
public class BoardServiceImpl implements BoardService {

	@Inject
	private BoardDAO dao;
 
	@Transactional
	@Override
	public void regist(BoardVO board) throws Exception {
		
		// 게시물 등록
		dao.regist(board);
		
		String[] files = board.getFiles();

		if(files == null) { return; } 

		// 첨부파일의 이름 배열을 이용하여 각각 파일 이름을 데이터베이스에 추가 
		for (String fileName : files) {
			dao.addAttach(fileName);
		}  
		
	}

	@Override
	public BoardVO read(Integer bno) throws Exception {
		return dao.read(bno);
	}

	@Override
	public void update(BoardVO board) throws Exception {
		dao.update(board);

		//...608p.
		Integer bno = board.getBno();

		dao.deleteAttach(bno);

		String[] files = board.getFiles();
		

		if(files == null) { return; } 

		for (String fileName : files) {			
			dao.replaceAttach(fileName, bno);
} 
	}

	@Transactional
	@Override
	public void remove(Integer bno) throws Exception {
		dao.deleteAttach(bno);
		dao.remove(bno);
	}

	@Override
	public List<BoardVO> listAll() throws Exception {
		return dao.listAll();
	}

	// @Override
	// public int countPaging(Criteria cri) throws Exception {
	// return dao.countPaging(cri);
	// }

	@Override
	public int listCountCriteria(Criteria cri) throws Exception {
		return dao.countPaging(cri);
	}

	@Override
	public List<BoardVO> listCriteria(Criteria cri) throws Exception {
		return dao.listCriteria(cri);
	}

	@Override
	public List<BoardVO> listSearchCriteria(SearchCriteria cri) throws Exception {
		return dao.listSearch(cri);
	}

	@Override
	public int listSearchCount(SearchCriteria cri) throws Exception {
		return dao.listSearchCount(cri);
	}

	@Override
	public List<String> getAttach(Integer bno) throws Exception {
		return dao.getAttach(bno);
	}
 
	
	
}