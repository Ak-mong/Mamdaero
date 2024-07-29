import React from 'react';
import { Routes, Route, useLocation } from 'react-router-dom';
import SignUpCounselorInput from '@/components/input/SignUpCounselorInput';
import SignUpCounselorInfo from '@/components/input/SignUpCounselorInfo';
import ProgressBar from '@/components/navigation/ProgressBar';

const SignUpCounselor: React.FC = () => {
  const location = useLocation();
  let currentStep: 'input' | 'info' | 'complete' = 'input';

  if (location.pathname.includes('info')) {
    currentStep = 'info';
  } else if (location.pathname.includes('complete')) {
    currentStep = 'complete';
  }

  return (
    <div className="flex flex-col items-center min-h-screen bg-orange-50 w-full pt-28">
      <div className="w-full max-w-3xl ">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold mb-3">상담사 회원 가입</h1>
          <ProgressBar currentStep={currentStep} />
        </div>
        <div className="flex justify-center">
          <Routes>
            <Route path="/" element={<SignUpCounselorInput />} />
            <Route path="info" element={<SignUpCounselorInfo />} />
          </Routes>
        </div>
      </div>
    </div>
  );
};

export default SignUpCounselor;
