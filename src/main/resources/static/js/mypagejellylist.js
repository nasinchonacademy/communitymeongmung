//
// document.addEventListener('DOMContentLoaded', function() {
//     const progressCircles = document.querySelectorAll('.progress-circle');
//     progressCircles.forEach(function(circle) {
//         const progress = parseInt(circle.getAttribute('data-progress'));
//         const angle = progress * 3.6; // 퍼센트를 각도로 변환
//         circle.style.background = `conic-gradient(
//                 #00c292 0deg ${angle}deg,
//                 #e0e0e0 ${angle}deg 360deg
//             )`;
//         // 원 안의 퍼센트 텍스트 업데이트
//         circle.textContent = progress + '%';
//     });
// });
