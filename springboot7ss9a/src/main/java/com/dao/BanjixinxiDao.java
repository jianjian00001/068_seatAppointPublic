package com.dao;

import com.entity.BanjixinxiEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.BanjixinxiVO;
import com.entity.view.BanjixinxiView;


/**
 * 班级信息
 * 
 * @author 
 * @email 
 * @date 2021-04-16 20:36:41
 */
public interface BanjixinxiDao extends BaseMapper<BanjixinxiEntity> {
	
	List<BanjixinxiVO> selectListVO(@Param("ew") Wrapper<BanjixinxiEntity> wrapper);
	
	BanjixinxiVO selectVO(@Param("ew") Wrapper<BanjixinxiEntity> wrapper);
	
	List<BanjixinxiView> selectListView(@Param("ew") Wrapper<BanjixinxiEntity> wrapper);

	List<BanjixinxiView> selectListView(Pagination page,@Param("ew") Wrapper<BanjixinxiEntity> wrapper);
	
	BanjixinxiView selectView(@Param("ew") Wrapper<BanjixinxiEntity> wrapper);
	
}
