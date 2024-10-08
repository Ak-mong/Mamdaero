package com.mamdaero.domain.counselor_item.repository;

import com.mamdaero.domain.counselor_item.entity.CounselorItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CounselorItemRepository extends JpaRepository<CounselorItem, Long> {

    List<CounselorItem> findByCounselorId(Long counselorItemId);
}