package jp.co.internous.quest.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.quest.model.domain.MstDestination;
import jp.co.internous.quest.model.domain.MstUser;
import jp.co.internous.quest.model.form.DestinationForm;
import jp.co.internous.quest.model.mapper.MstDestinationMapper;
import jp.co.internous.quest.model.mapper.MstUserMapper;
import jp.co.internous.quest.model.session.LoginSession;

@Controller
@RequestMapping("/quest/destination")
public class DestinationController {

	@Autowired
	private LoginSession loginSession;

	@Autowired
	private MstUserMapper userMapper;

	@Autowired
	private MstDestinationMapper destinationMapper;

	private Gson gson = new Gson();

	@RequestMapping("/")
	public String index(Model m) {
		/* 宛先姓名のデフォルト表示用データを取得する */
		MstUser user = userMapper.findByUserNameAndPassword(loginSession.getUserName(), loginSession.getPassword());
		m.addAttribute("user", user);
		/* page_header.html用にloginSessionを格納する */
		m.addAttribute("loginSession", loginSession);
		return "destination";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/delete")
	@ResponseBody
	public boolean delete(@RequestBody String destinationId) {
		Map<String, String> map = gson.fromJson(destinationId, Map.class);
		String id = map.get("destinationId");
		/* 論理削除を実行し、更新行数を取得する */
		int result = destinationMapper.logicalDeleteById(Integer.parseInt(id));
		/* 削除成功でtrue、削除失敗でfalseを返却する */
		return result > 0;
	}

	@RequestMapping("/register")
	@ResponseBody
	public String register(@RequestBody DestinationForm f) {
		MstDestination d = new MstDestination(f);
		d.setUserId(loginSession.getUserId());
		int count = destinationMapper.insert(d);

		Integer id = 0;
		if(count > 0) {
			id = d.getId();
		}
		/* 登録した宛先情報のID (destinationId) を返却する */
		return id.toString();
	}

}
