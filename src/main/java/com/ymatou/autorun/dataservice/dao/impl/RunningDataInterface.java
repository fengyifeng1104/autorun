package com.ymatou.autorun.dataservice.dao.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ymatou.autorun.dataservice.model.RunningDataModel;

public interface RunningDataInterface extends JpaRepository<RunningDataModel,Integer>{

}
