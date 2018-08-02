package org.zerock.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardVO;
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
	   public String createPOST(BoardVO board, Model model) throws Exception {
	  
	   logger.info("regist post ...........");
	   logger.info(board.toString());
	  
	   service.regist(board);
	  
	 
	   model.addAttribute("result", "success");
//	   return "/board/success";
	   return "redirect:/board/listAll";
	   
	   }	
	   
	   @RequestMapping(value = "/listAll", method = RequestMethod.GET)
	   public void listAll(Model model) throws Exception {
		    logger.info("show all list......................");
		    model.addAttribute("list", service.listAll());
	   }	
	   
	   @RequestMapping(value = "/read", method = RequestMethod.GET)	   
	   public void read(@RequestParam("bno") int bno, Model model) throws Exception {
		    model.addAttribute(service.read(bno));
	   }
	   

	   @RequestMapping(value = "/remove", method = RequestMethod.POST)
	   public String delete(@RequestParam("bno") int bno, RedirectAttributes rttr) 
			   throws Exception {
		    	
		service.remove(bno);
		
		rttr.addFlashAttribute("msg", "SUCCESS");
		
		logger.info("삭제처리, bno = " + bno);    
		
		return "redirect:/board/listAll";
	   }
	   
	   @RequestMapping(value = "/modify", method = RequestMethod.GET)
	   public void updateGET(@RequestParam("bno") int bno, Model model) throws Exception {
		   logger.info("수정페이지로 이동, update/ get/ bno = " + bno);  		   
		   model.addAttribute(service.read(bno));
	   }
	   

	   @RequestMapping(value = "/modify", method = RequestMethod.POST)
	   public String updatePOST(BoardVO board, RedirectAttributes rttr) throws Exception {

	     logger.info("수정처리, update post BoardVO = " + board.toString());

	     service.update(board);
	     rttr.addFlashAttribute("msg", "SUCCESS");

	     return "redirect:/board/listAll";
	   }

	   
	   
}