package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.XinyongfenEntity;
import com.entity.view.XinyongfenView;

import com.service.XinyongfenService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;


/**
 * 信用分
 * 后端接口
 * @author 
 * @email 
 * @date 2021-04-16 20:36:41
 */
@RestController
@RequestMapping("/xinyongfen")
public class XinyongfenController {
    @Autowired
    private XinyongfenService xinyongfenService;
    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,XinyongfenEntity xinyongfen,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("xuesheng")) {
			xinyongfen.setXuehao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<XinyongfenEntity> ew = new EntityWrapper<XinyongfenEntity>();
		PageUtils page = xinyongfenService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, xinyongfen), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,XinyongfenEntity xinyongfen, HttpServletRequest request){
        EntityWrapper<XinyongfenEntity> ew = new EntityWrapper<XinyongfenEntity>();
		PageUtils page = xinyongfenService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, xinyongfen), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( XinyongfenEntity xinyongfen){
       	EntityWrapper<XinyongfenEntity> ew = new EntityWrapper<XinyongfenEntity>();
      	ew.allEq(MPUtil.allEQMapPre( xinyongfen, "xinyongfen")); 
        return R.ok().put("data", xinyongfenService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(XinyongfenEntity xinyongfen){
        EntityWrapper< XinyongfenEntity> ew = new EntityWrapper< XinyongfenEntity>();
 		ew.allEq(MPUtil.allEQMapPre( xinyongfen, "xinyongfen")); 
		XinyongfenView xinyongfenView =  xinyongfenService.selectView(ew);
		return R.ok("查询信用分成功").put("data", xinyongfenView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        XinyongfenEntity xinyongfen = xinyongfenService.selectById(id);
        return R.ok().put("data", xinyongfen);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        XinyongfenEntity xinyongfen = xinyongfenService.selectById(id);
        return R.ok().put("data", xinyongfen);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody XinyongfenEntity xinyongfen, HttpServletRequest request){
    	xinyongfen.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(xinyongfen);
        xinyongfenService.insert(xinyongfen);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody XinyongfenEntity xinyongfen, HttpServletRequest request){
    	xinyongfen.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(xinyongfen);
        xinyongfenService.insert(xinyongfen);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody XinyongfenEntity xinyongfen, HttpServletRequest request){
        //ValidatorUtils.validateEntity(xinyongfen);
        xinyongfenService.updateById(xinyongfen);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        xinyongfenService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 提醒接口
     */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request, 
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {
		map.put("column", columnName);
		map.put("type", type);
		
		if(type.equals("2")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date remindStartDate = null;
			Date remindEndDate = null;
			if(map.get("remindstart")!=null) {
				Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
				c.setTime(new Date()); 
				c.add(Calendar.DAY_OF_MONTH,remindStart);
				remindStartDate = c.getTime();
				map.put("remindstart", sdf.format(remindStartDate));
			}
			if(map.get("remindend")!=null) {
				Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH,remindEnd);
				remindEndDate = c.getTime();
				map.put("remindend", sdf.format(remindEndDate));
			}
		}
		
		Wrapper<XinyongfenEntity> wrapper = new EntityWrapper<XinyongfenEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("xuesheng")) {
			wrapper.eq("xuehao", (String)request.getSession().getAttribute("username"));
		}

		int count = xinyongfenService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	


}
