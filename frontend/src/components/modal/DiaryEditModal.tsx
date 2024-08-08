import React, { useState } from 'react';
import { Emotion, getEmotionImage, emotionImages } from '@/pages/emotiondiary/emotion';
import ModalWrapper from '@/components/modal/ModalWrapper';

interface DiaryEditModalProps {
  isOpen: boolean;
  diary: {
    id: string;
    date: string;
    emotion: Emotion;
    content: string;
    shareWithCounselor: boolean;
  };
  onClose: () => void;
  onSubmit: (diary: {
    id: string;
    date: string;
    emotion: Emotion;
    content: string;
    shareWithCounselor: boolean;
  }) => void;
}

const DiaryEditModal: React.FC<DiaryEditModalProps> = ({ isOpen, diary, onClose, onSubmit }) => {
  if (!diary) return null; // diary가 null이면 아무것도 렌더링하지 않음

  const [emotion, setEmotion] = useState<Emotion>(diary.emotion);
  const [content, setContent] = useState(diary.content);
  const [shareWithCounselor, setShareWithCounselor] = useState(diary.shareWithCounselor);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit({
      ...diary,
      emotion,
      content,
      shareWithCounselor,
    });
  };

  return (
    <ModalWrapper isOpen={isOpen} onClose={onClose}>
      <div className="bg-gray-100 p-4 rounded-lg shadow-lg" style={{ width: '600px' }}>
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-lg font-bold">날짜</h2>
          <div className="bg-orange-200 px-4 py-2 rounded">{diary.date}</div>
          <div className="flex items-center">
            <span className="mr-2">오늘의 감정</span>
            {Object.keys(emotionImages).map(key => (
              <button
                key={key}
                type="button"
                className={`p-1 rounded-full ${emotion === key ? 'bg-orange-300' : ''}`}
                onClick={() => setEmotion(key as Emotion)}
              >
                <img src={getEmotionImage(key as Emotion)} alt={key} className="w-7 h-6" />
              </button>
            ))}
          </div>
        </div>

        <div className="mb-4 flex items-center">
          <input
            type="checkbox"
            id="shareWithCounselor"
            checked={shareWithCounselor}
            onChange={e => setShareWithCounselor(e.target.checked)}
            className="mr-2"
          />
          <label htmlFor="shareWithCounselor">상담사 공개</label>
        </div>

        <form onSubmit={handleSubmit}>
          <textarea
            className="w-full p-4 border rounded-lg bg-white h-40 resize-none"
            value={content}
            onChange={e => setContent(e.target.value)}
            placeholder="오늘의 일기를 작성해주세요..."
            required
          />

          <div className="flex justify-center mt-4">
            <button type="submit" className="px-6 py-2 bg-orange-300 text-white rounded-full">
              작성 완료
            </button>
          </div>
        </form>
      </div>
    </ModalWrapper>
  );
};

export default DiaryEditModal;
