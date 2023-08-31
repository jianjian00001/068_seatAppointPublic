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

import com.entity.QiandaoxinxiEntity;
import com.entity.view.QiandaoxinxiView;

import com.service.QiandaoxinxiService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;


/**
 * 签到信息
 * 后端接口
 * @author 
 * @email 
 * @date 2021-04-16 20:36:41
 */
@RestController
@RequestMapping("/qiandaoxinxi")
public class QiandaoxinxiController {
    @Autowired
    private QiandaoxinxiService qiandaoxinxiService;
    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,QiandaoxinxiEntity qiandaoxinxi,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("xuesheng")) {
			qiandaoxinxi.setXuehao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<QiandaoxinxiEntity> ew = new EntityWrapper<QiandaoxinxiEntity>();
		PageUtils page = qiandaoxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, qiandaoxinxi), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,QiandaoxinxiEntity qiandaoxinxi, HttpServletRequest request){
        EntityWrapper<QiandaoxinxiEntity> ew = new EntityWrapper<QiandaoxinxiEntity>();
		PageUtils page = qiandaoxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, qiandaoxinxi), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( QiandaoxinxiEntity qiandaoxinxi){
       	EntityWrapper<QiandaoxinxiEntity> ew = new EntityWrapper<QiandaoxinxiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( qiandaoxinxi, "qiandaoxinxi")); 
        return R.ok().put("data", qiandaoxinxiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(QiandaoxinxiEntity qiandaoxinxi){
        EntityWrapper< QiandaoxinxiEntity> ew = new EntityWrapper< QiandaoxinxiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( qiandaoxinxi, "qiandaoxinxi")); 
		QiandaoxinxiView qiandaoxinxiView =  qiandaoxinxiService.selectView(ew);
		return R.ok("查询签到信息成功").put("data", qiandaoxinxiView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        QiandaoxinxiEntity qiandaoxinxi = qiandaoxinxiService.selectById(id);
        return R.ok().put("data", qiandaoxinxi);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        QiandaoxinxiEntity qiandaoxinxi = qiandaoxinxiService.selectById(id);
        return R.ok().put("data", qiandaoxinxi);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody QiandaoxinxiEntity qiandaoxinxi, HttpServletRequest request){
    	qiandaoxinxi.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(qiandaoxinxi);
        qiandaoxinxiService.insert(qiandaoxinxi);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody QiandaoxinxiEntity qiandaoxinxi, HttpServletRequest request){
    	qiandaoxinxi.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(qiandaoxinxi);
        qiandaoxinxiService.insert(qiandaoxinxi);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody QiandaoxinxiEntity qiandaoxinxi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(qiandaoxinxi);
        qiandaoxinxiService.updateById(qiandaoxinxi);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        qiandaoxinxiService.deleteBatchIds(Arrays.asList(ids));
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
		
		Wrapper<QiandaoxinxiEntity> wrapper = new EntityWrapper<QiandaoxinxiEntity>();
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

		int count = qiandaoxinxiService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	


}
