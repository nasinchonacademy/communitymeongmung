document.addEventListener("DOMContentLoaded", function() {
    fetch('/api/stories')
        .then(response => response.json())
        .then(data => {
            const stories = data; // JSON 데이터를 stories 배열에 할당
            const carouselInner = document.getElementById("story-container");
            let startIndex = 0;

            // 문자열 자르기 함수
            function truncateString(str, num) {
                return str.length > num ? str.slice(0, num) + '...' : str;
            }

            function updateSlide() {
                carouselInner.innerHTML = ''; // 슬라이드 초기화

                // 슬라이드의 끝까지 3개씩 그룹화하여 슬라이드를 생성
                while (startIndex < stories.length) {
                    const endIndex = Math.min(startIndex + 3, stories.length); // 3개의 아이템을 한 그룹으로 묶음
                    const item = document.createElement('div');
                    item.className = "carousel-item"; // 각 그룹이 하나의 슬라이드
                    if (startIndex === 0) {
                        item.classList.add('active'); // 첫 번째 슬라이드를 활성화 상태로 설정
                    }

                    const row = document.createElement('div');
                    row.className = "row"; // 가로로 정렬하기 위해 row 클래스 추가

                    // 현재 슬라이드에 표시할 3개의 게시물을 가져옴
                    for (let i = startIndex; i < endIndex; i++) {
                        const story = stories[i];
                        const truncatedContent = truncateString(story.content, 10); // 컨텐츠 길이 제한

                        // 글 상세보기 페이지 링크 설정
                        const storyUrl = `/story/${story.seq}`;

                        const col = document.createElement("div");
                        col.className = "col-md-4";
                        col.innerHTML = `
                            <a href="${storyUrl}" class="text-decoration-none text-reset"> <!-- 카드 전체를 링크로 감싸기 -->
                                <div class="card mx-auto" style="width: 12rem;">
                                    <img src="${story.picture || '/image/tool/storydefault.PNG'}" class="card-img-top" alt="${story.title}">
                                    <div class="card-body p-2">
                                        <h6 class="card-title">${story.title}</h6>
                                        <p class="card-text mb-1" style="font-size: 0.85rem;">${story.likecount} Likes</p>
                                        <p class="card-text" style="font-size: 0.85rem;">${truncatedContent}</p> <!-- 잘린 컨텐츠 표시 -->
                                        <div class="d-flex justify-content-between align-items-center mt-2">
                                            <small class="text-muted" style="font-size: 0.75rem;">댓글 ${story.commentcount}</small>
                                            <i class="bi bi-heart-fill text-danger"></i>
                                        </div>
                                    </div>
                                    <div class="card-footer p-2">
                                        <small class="text-muted">작성자: ${story.nickname}</small>
                                    </div>
                                </div>
                            </a>`;
                        row.appendChild(col);
                    }

                    item.appendChild(row);
                    carouselInner.appendChild(item);
                    startIndex += 3; // 다음 그룹으로 이동
                }
            }

            document.getElementById("nextBtn").addEventListener("click", function(event) {
                event.preventDefault();
                const activeItem = document.querySelector('.carousel-item.active');
                const nextItem = activeItem.nextElementSibling;
                if (nextItem) {
                    activeItem.classList.remove('active');
                    nextItem.classList.add('active');
                }
            });

            document.getElementById("prevBtn").addEventListener("click", function(event) {
                event.preventDefault();
                const activeItem = document.querySelector('.carousel-item.active');
                const prevItem = activeItem.previousElementSibling;
                if (prevItem) {
                    activeItem.classList.remove('active');
                    prevItem.classList.add('active');
                }
            });

            updateSlide();
        })
        .catch(error => console.error('Error fetching stories:', error));
});
