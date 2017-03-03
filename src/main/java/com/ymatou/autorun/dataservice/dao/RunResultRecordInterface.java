package com.ymatou.autorun.dataservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ymatou.autorun.dataservice.model.RunResultRecordModel;

public interface RunResultRecordInterface extends JpaRepository<RunResultRecordModel, Integer>{

}
