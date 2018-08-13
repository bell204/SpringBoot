package org.zerock.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageMaker;
import org.zerock.service.BoardService;

  
@Controller
@RequestMapping("/board/*")
public class BoardController {
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

	@Inject
	private BoardService service;

	 
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public void createGET(BoardVO board, Model model) throws Exception {
		  logger.info("register get ...........");

	}
 
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String createPOST(BoardVO board, RedirectAttributes rattr) throws Exception {
		logger.info("regist post ...........");
		logger.info(board.toString());

		service.regist(board);

		rattr.addFlashAttribute("msg", "SUCCESS");

		return "redirect:/board/listAll";

	}

 
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public void listAll(Model model) throws Exception {
		logger.info("show all list......................");
		model.addAttribute("list", service.listAll());
	}
	
	 
	
	@RequestMapping(value = "/listCri", method = RequestMethod.GET)	
	public void listAll(Criteria cri, Model model) throws Exception {
		logger.info("show list Page with Criteria......................");
		model.addAttribute("list", service.listCriteria(cri)); // p262
	}	

	 
	// p292
	@RequestMapping(value = "/read", method = RequestMethod.GET)
	public void read(@RequestParam("bno") int bno, 
					 Model model) throws Exception {
		logger.info("read called......................");
		model.addAttribute(service.read(bno));
	}
 
	// p293
	  @RequestMapping(value = "/readPage", method = RequestMethod.GET)
	  public void read(@RequestParam("bno") int bno, 
			  		   @ModelAttribute("cri") Criteria cri, 
			  		   Model model) throws Exception {
		logger.info("readPage called......................");
	    model.addAttribute(service.read(bno));
	  }
	
	 
	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	public String delete(@RequestParam("bno") int bno, 
						 RedirectAttributes rttr) throws Exception {

		logger.info("delete called... bno = " + bno);
		
		service.remove(bno);

		rttr.addFlashAttribute("msg", "SUCCESS");


		return "redirect:/board/listAll";
	}

	 // p296
	@RequestMapping(value = "/removePage", method = RequestMethod.POST)
	public String delete(@RequestParam("bno") int bno, 
						 Criteria cri,
						 RedirectAttributes rttr) throws Exception {

		logger.info("deletePage POST called... bno = " + bno + cri.toString());
		
		service.remove(bno);

	    rttr.addAttribute("page", cri.getPage());
	    rttr.addAttribute("perPageNum", cri.getPerPageNum());
	    rttr.addFlashAttribute("msg", "SUCCESS");		
		

		return "redirect:/board/listPage";
	}
	
	
	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public void updateGET(@RequestParam("bno") int bno, Model model) throws Exception {
		model.addAttribute(service.read(bno));
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String updatePOST(BoardVO board, RedirectAttributes rttr) throws Exception {


		service.update(board);
		rttr.addFlashAttribute("msg", "SUCCESS");

		return "redirect:/board/listAll";
	}

	// p298
	@RequestMapping(value = "/modifyPage", method = RequestMethod.GET)
	public void updatePageGET(@RequestParam("bno") int bno, 
							    @ModelAttribute("cri") Criteria cri, 
							    Model model) throws Exception {
		model.addAttribute(service.read(bno));
	}
	
	// p300
	@RequestMapping(value = "/modifyPage", method = RequestMethod.POST)
	public String updatePagePOST(BoardVO board, 
					  			Criteria cri, 
					  			RedirectAttributes rttr) throws Exception {
	  service.update(board);
	  rttr.addAttribute("page", cri.getPage());
	  rttr.addAttribute("perPageNum", cri.getPerPageNum());
	  rttr.addFlashAttribute("msg", "SUCCESS");
  
	  return "redirect:/board/listPage";
	  
	}  
  	
	
	 
	@RequestMapping(value = "/listPage", method = RequestMethod.GET)
	public void listPage(@ModelAttribute("cri") Criteria cri, Model model) throws Exception { 

		logger.info("listPage called..." + cri.toString());

		model.addAttribute("list", service.listCriteria(cri));
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(service.listCountCriteria(cri)); 

		model.addAttribute("pageMaker", pageMaker);
	}

}