import React, { useState } from 'react';
import CounselorCard from '@/components/card/CounselorCard';
import AlignDropdown from '@/components/dropdown/AlignDropdown';
import { IoIosSearch } from 'react-icons/io';

const CounselorListPage: React.FC = () => {
  const [selectedOption1, setSelectedOption1] = useState('이름 순');
  const [selectedOption2, setSelectedOption2] = useState('성별 무관');
  const [selectedOption3, setSelectedOption3] = useState('모든 급수');

  const options1 = ['이름 순', '리뷰 많은 순', '평점 높은 순'];
  const options2 = ['성별 무관', '남자 상담사', '여자 상담사'];
  const options3 = ['모든 급수', '1급 상담사', '2급 상담사'];

  return (
    <div>
      <div className="flex flex-col xl:flex-row justify-between my-5">
        <div>
          <div className="font-bold text-xl sm:text-2xl md:text-3xl text-orange-500">
            맘대로 상담사 조회
          </div>
          <div className="my-2 text-sm sm:text-base md:text-lg">
            나에게 딱 맞는 상담사를 만나보세요.
          </div>
        </div>
        <div className="flex items-center">
          <AlignDropdown
            selectedOption={selectedOption1}
            options={options1}
            onOptionClick={setSelectedOption1}
          />
          <AlignDropdown
            selectedOption={selectedOption2}
            options={options2}
            onOptionClick={setSelectedOption2}
          />
          <AlignDropdown
            selectedOption={selectedOption3}
            options={options3}
            onOptionClick={setSelectedOption3}
          />
          <label className="input input-bordered flex items-center ms-5">
            <input type="text" className="w-60" placeholder="상담사를 검색하세요." />
            <IoIosSearch />
          </label>
        </div>
      </div>
      <div className="grid grid-cols-2 gap-6">
        <CounselorCard
          counselorID="1234"
          counselorName="박민준"
          counselorIntro="안녕하세요. 박민준 상담사 입니다. 안녕하세요."
          reviewAvgScore={4.5}
          reviewCount={12}
        />
        <CounselorCard
          counselorID="1234"
          counselorName="박민준"
          counselorIntro="안녕하세요. 박민준 상담사 입니다. 안녕하세요."
          reviewAvgScore={4.5}
          reviewCount={12}
        />
        <CounselorCard
          counselorID="1234"
          counselorName="박민준"
          counselorIntro="안녕하세요. 박민준 상담사 입니다. 안녕하세요."
          reviewAvgScore={4.5}
          reviewCount={12}
        />
      </div>
    </div>
  );
};

export default CounselorListPage;
