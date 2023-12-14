package com.example.shoppingmall_project.Controller;

import com.example.shoppingmall_project.model.vo.mypagevo.*;
import com.example.shoppingmall_project.service.MyPageService;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping(value="/mypage")
public class MypagController
{	
	@Autowired 
	private MyPageService myPageService;
	
	@Autowired
	private OrderVO orderVO;
	//private MemberVO memberVO;

	@Autowired
	private Cart_vo cartVO ;
	@RequestMapping(value="/cancelMyOrder", method = RequestMethod.POST)
	public String cancelMyOrder(Model model, HttpSession httpsession, @RequestParam("orders_idx") String orders_idx)throws Exception
	{
		myPageService.deleteMyOrder(orders_idx);
		httpsession.setAttribute("message", "cancel_order");//myPageMain에서 message에 cancel_order가 바인딩 되어있으면
		//주문이 종료되었다는 안내문 뜬다.
		return "redirect:/mypage/myPageMain";
	}
	
	@RequestMapping(value="/myCartList", method = RequestMethod.GET)
	public String myCartList(Model model, HttpSession httpsession, HttpServletRequest request) throws Exception
	{
		HttpSession session=request.getSession();
		//MemberVO memberVO=(MemberVO)session.getAttribute("memberInfo");
		//int members_idx=memberVO.getMembers_idx();//세션소유자의 카트 목록 불러올 예정
		List<Cart_vo> cartList=myPageService.myCartList(1);//세션memeber_idx의 cart 리스트를 불러온다.
		session.setAttribute("cartList", cartList);//카트row들과  카트에 들어있는 상품목록 가져온다.
		//mav.addObject("cartMap", cartMap);
		return "mypage/myCartList";
	}

	@RequestMapping(value="/modifyCartQty" ,method = RequestMethod.POST)
	public @ResponseBody String  modifyCartQty(@RequestParam("cart_idx") int cart_idx,
											   @RequestParam("cart_goods_qty") int cart_goods_qty,
											   HttpServletRequest request, HttpServletResponse response)  throws Exception
	{
		MemberVO memberVO;
		HttpSession session=request.getSession();
		//memberVO=(MemberVO)session.getAttribute("memberInfo");
		memberVO = myPageService.givememember(1);
		int members_idx=memberVO.getMembers_idx();
		cartVO.setCart_idx(cart_idx);
		cartVO.setMembers_idx(members_idx);
		cartVO.setQuantity(cart_goods_qty);
		boolean result=myPageService.modifyCartQty(cartVO);
		System.out.println("cartVO.members_idx:"+cartVO.getMembers_idx());
		System.out.println("cart_goods_qty:"+cart_goods_qty);
		System.out.println("cartVO.quantity:"+cartVO.getQuantity());
		if(result==true){
			return "modify_success";
		}else{
			return "modify_failed";
		}

	}
	
	@RequestMapping(value = "/myPageMain", method = RequestMethod.GET)
	public String myPageMain(Model model, HttpSession httpsession
			,HttpServletRequest request, @RequestParam(required = false,value="message")  String message)throws Exception
	{
		ModelAndView mav = new ModelAndView();
		//memberVO = (MemberVO)httpsession.getAttribute("memberInfo");
		//String members_idx = memberVO.getMembers_idx()+"";
		
		//�α��� �����Ǹ� �� ���� �ּ� Ǯ�� members_idx�޾ƿ��� �ȴ�
		List<O_P_OD_vo> myOrderList = myPageService.listMyOrderGoods("1");
		httpsession.setAttribute("myOrderList", myOrderList);
		httpsession.setAttribute("message",message);//cancelMyOrder에서 온다.
		System.out.println("myOrderList size: "+myOrderList.size());
		return "mypage/myPageMain";
	}
	@RequestMapping(value = "/myDetailInfo", method = RequestMethod.GET)
	public String myDetailInfo(Model model, HttpSession httpsession
			,HttpServletRequest request)throws Exception
	{
		ModelAndView mav = new ModelAndView();
		

		//MemberVO temp_member = httpsession.getAttribute("memberInfo");
		MemberVO temp_member = myPageService.givememember(3);
		httpsession.setAttribute("memberInfo", temp_member);
		
		return "mypage/myDetailInfo";
	}
	

	// https://velog.io/@dhk22/Spring-MVC-4-Response-ViewResolver-ModelAndView-ResponseEntity-Response-Json-ing
	// https://velog.io/@wonizizi99/TIL-22.12.19-3Tier-controller-%EC%99%80RestController%EB%8F%99%EC%9E%91%EC%9B%90%EB%A6%AC-ResponseEntity-Httpstatus
	// https://m.blog.naver.com/nsqfrnidzb/222437153414 RestController����  ResponseEntity���� ��ȭ
	@RequestMapping(value="/modifyMyInfo.do" ,method = RequestMethod.POST)
	public ResponseEntity modifyMyInfo(@RequestParam("attribute")  String attribute,
			                 @RequestParam("value")  String value, Model model,
			               HttpServletRequest request, HttpServletResponse response)  throws Exception 
	{
		
		Map<Object,Object> memberMap=new HashMap<Object,Object>();
		String val_list[]=null;
		HttpSession session=request.getSession();
		

		MemberVO memberVO=(MemberVO)session.getAttribute("memberInfo");
		String  member_idx=memberVO.getMembers_idx()+"";
		
		String temp;
		if(attribute.equals("tel"))
		{
			value=value.replaceAll(",","-");
			System.out.println("value:"+value);
			memberMap.put("members_phone_number",value);
		}
		else if(attribute.equals("email")){
			val_list=value.split(",");
			value = val_list[0]+"@"+val_list[1]; 
			memberMap.put("members_email",value);
		}
		else if(attribute.equals("address")){
			val_list=value.split(",");
			memberMap.put("members_address",val_list[0]);
			memberMap.put("members_detailed_address",val_list[1]);
		}
		else //password, nickname, 
		{
			memberMap.put(attribute,value);	
		}
		
		memberMap.put("members_idx", member_idx);

		

		memberVO=(MemberVO)myPageService.modifyMyInfo(memberMap);
		session.removeAttribute("memberInfo");
		session.setAttribute("memberInfo", memberVO);
		
		//return "mypage/myPageMain";
		String message = null;
		ResponseEntity resEntity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		message  = "mod_success";
		resEntity =new ResponseEntity(message, responseHeaders, HttpStatus.OK);
		return resEntity;
	}
	
	@RequestMapping(value="/myOrderDetail" ,method = RequestMethod.GET)
	public String myOrderDetail(HttpSession httpsession, Model model, @RequestParam("orders_idx") int orders_idx )
	throws Exception
	{
		O_OD_P_C_S_M_vo myorderdetail =  myPageService.getOrderDetail(orders_idx);
		httpsession.setAttribute("myorderdetail", myorderdetail );
		
		return "/mypage/myOrderDetail";
	}
	
	
	
	private static String CURR_IMAGE_REPO_PATH = "C:\\shopping\\file_repo";//�ش��� �Ʒ��� ��ǰ idx\img_url�������� �̹��� �־��ָ� �ȴ�.
	
	@RequestMapping("/thumbnails")
	protected void thumbnails(@RequestParam("fileName") String fileName,
                            	@RequestParam("goods_id") String goods_id,
			                 HttpServletResponse response) throws Exception {
		OutputStream out = response.getOutputStream();
		String filePath=CURR_IMAGE_REPO_PATH+"\\"+goods_id+"\\"+fileName;

		File image=new File(filePath);

		if (image.exists()) { 
			//https://yermi.tistory.com/entry/Library-%EC%9E%90%EB%B0%94Java%EC%97%90%EC%
			//84%9C-%EC%8D%B8%EB%84%A4%EC%9D%BC-%EC%9D%B4%EB%AF%B8%EC%A7%80
			//-%EB%A7%8C%EB%93%A4%EA%B8%B0-%EC%8D%B8%EB%84%A4%EC%9D%BC-%EB%9
			//D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC-Thumbnailator -->
			Thumbnails.of(image).size(121,154).outputFormat("png").toOutputStream(out);
		}
		byte[] buffer = new byte[1024 * 8];
		out.write(buffer);
		out.close();
	}

	
}