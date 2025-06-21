import matplotlib
from flask import Flask, request, send_file, jsonify
import librosa
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import numpy as np
import io
import os
import soundfile as sf


app = Flask(__name__)

# 이미지 형태로 분석 결과 반환
@app.route('/analyze', methods=['POST'])
def analyze():
    # 1. 파일 저장
    data = request.get_data()
    with open("temp.wav", "wb") as f:
        f.write(data)
    print("temp.wav 크기:", os.path.getsize("temp.wav"))

    # 디버깅용
    sf_data, sf_sr = sf.read("temp.wav")
    print("sf_data:", sf_data.shape, sf_sr)
    print("샘플 일부:", sf_data[:10])

    # 2. 주파수 분석
    y, sr = librosa.load("temp.wav", sr=None)
    pitches, magnitudes = librosa.piptrack(y=y, sr=sr)
    print("y sample:", y[:10])

    # 3. 그래프 생성
    fig, ax = plt.subplots(figsize=(15, 6))
    # 개선: 0이 아닌 값만 추출해서 윤곽 생성
    pitch_track = []
    for t in range(pitches.shape[1]):
        index = magnitudes[:, t].argmax()
        pitch = pitches[index, t]
        pitch_track.append(pitch)
    pitch_track = np.array(pitch_track)

    # 정규화: y축을 0 ~ 12 범위로 단순화
    pitch_track = pitch_track / np.max(pitch_track) * 10  # 또는 12
    #ax.plot(np.max(pitches, axis=0))  # 프레임별 최대 피치 시각화
    # ax.plot(np.mean(pitches, axis=0))  # 간단히 평균 피치 시각화
    ax.plot(pitch_track, color='black', linewidth=2)
    ax.set_yticks(np.arange(0, 13, 2))
    ax.set_xticks([])
    ax.grid(True, linestyle=':', linewidth=0.5)
    for side in ['top', 'right']:
        ax.spines[side].set_visible(False)
    for side in ['left', 'bottom']:
        ax.spines[side].set_linewidth(1)
    ax.set_title("Pitch Over Time")
    ax.set_xlabel("Frame")
    ax.set_ylabel("Frequency (Hz)")

    # 4. PNG 변환 후 응답
    img_io = io.BytesIO()
    plt.savefig(img_io, format='png')
    img_io.seek(0)
    plt.close(fig)

    return send_file(img_io, mimetype='image/png')


# json 형태로 분석 결과 반환
@app.route('/emotion', methods=['POST'])
def emotion():
    print("Flask /emotion 엔드포인트 호출됨")
    print(f"Request files: {request.files}") # 💡 추가: Flask가 받은 파일 파트
    # 1. 파일 저장
    data = request.get_data()
    with open("temp.wav", "wb") as f:
        f.write(data)

    # 2. 주파수 분석
    y, sr = librosa.load("temp.wav", sr=None)
    pitches, magnitudes = librosa.piptrack(y=y, sr=sr)

    # 3. 피치 값 추출 (간단한 분석용)
    pitch_track = []
    for t in range(pitches.shape[1]):
        index = magnitudes[:, t].argmax()
        pitch = pitches[index, t]
        pitch_track.append(pitch)
    pitch_track = [float(p) for p in pitch_track if p > 0.0]

    # 4. JSON 형태로 응답
    result = {
        "pitch_mean": np.mean(pitch_track),
        "pitch_max": np.max(pitch_track),
        "pitch_min": np.min(pitch_track),
        "sample_count": len(pitch_track)
    }

    return jsonify(result)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5050)