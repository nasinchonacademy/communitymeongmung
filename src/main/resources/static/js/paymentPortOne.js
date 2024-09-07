// 포트원
var IMP = window.IMP;
IMP.init('imp73447046')

function requestPay() {
    var IMP = window.IMP;
    IMP.init('imp73447046')

    // 사용자로부터 폼 데이터를 가져오기
    var resname = document.getElementById('resname').value;
    var resphone = document.getElementById('resphone').value;
    var postcode = document.getElementById('postcode').value;
    var roadaddress = document.getElementById('roadaddress').value;
    var jibunaddress = document.getElementById('jibunaddress').value;
    var totalprice = document.getElementById('totalprice').value;
    var productname = document.querySelector('.product-info span').innerText; // 상품명 가져오기

    // 포트원 결제창 호출
    IMP.request_pay(
        {
            pg: "kakaopay.TC0ONETIME",
            pay_method: "card",
            merchant_uid: "merchant_" + new Date().getTime(),  // 동적으로 생성된 주문번호
            name: productname,  // 상품명
            amount: totalprice,  // 결제할 금액
            buyer_email: "example@example.com",  // 예시: 실제로는 사용자 이메일을 가져와야 함
            buyer_name: resname,  // 받는 사람 이름
            buyer_tel: resphone,  // 연락처
            buyer_addr: roadaddress + " " + jibunaddress,  // 도로명 주소와 지번 주소를 합침
            buyer_postcode: postcode,  // 우편번호
        },
        function (rsp) {
            if (rsp.success) {
                // 결제 성공 시 폼 제출
                document.querySelector('form').submit();
            } else {
                alert("결제에 실패하였습니다. 에러: " + rsp.error_msg);
            }
        });
}

function requestCardPay() {
    var IMP = window.IMP;
    IMP.init('imp73447046')

    var resname = document.getElementById('resname').value;
    var resphone = document.getElementById('resphone').value;
    var postcode = document.getElementById('postcode').value;
    var roadaddress = document.getElementById('roadaddress').value;
    var jibunaddress = document.getElementById('jibunaddress').value;
    var totalprice = document.getElementById('totalprice').value;
    var productname = document.querySelector('.product-info span').innerText; // 상품명 가져오기

    IMP.request_pay(
        {
            pg: "html5_inicis.INIpayTest", //테스트 시 html5_inicis.INIpayTest 기재
            pay_method: "card",
            merchant_uid: "merchant_" + new Date().getTime(), //상점에서 생성한 고유 주문번호
            name: productname,  // 상품명
            amount: totalprice,  // 결제할 금액
            buyer_email: "test@portone.io",
            buyer_name: resname,  // 받는 사람 이름
            buyer_tel: resphone,  // 연락처
            buyer_addr: roadaddress + " " + jibunaddress,  // 도로명 주소와 지번 주소를 합침
            buyer_postcode: postcode,  // 우편번호
            // m_redirect_url: "{모바일에서 결제 완료 후 리디렉션 될 URL}",
            escrow: true, //에스크로 결제인 경우 설정
            vbank_due: "YYYYMMDD",
            bypass: {
                // PC 경우
                acceptmethod: "noeasypay", // 간편결제 버튼을 통합결제창에서 제외(PC)
                // acceptmethod: "cardpoint", // 카드포인트 사용시 설정(PC)
                // 모바일 경우
                P_RESERVED: "noeasypay=Y", // 간편결제 버튼을 통합결제창에서 제외(모바일)
                // P_RESERVED: "cp_yn=Y", // 카드포인트 사용시 설정(모바일)
                // P_RESERVED: "twotrs_bank=Y&iosapp=Y&app_scheme=your_app_scheme://", // iOS에서 계좌이체시 결제가 이뤄지던 앱으로 돌아가기
            },
            period: {
                from: "20200101", //YYYYMMDD
                to: "20201231", //YYYYMMDD
            },
        },
        function (rsp) {
            if (rsp.success) {
                // 결제 성공 시 폼 제출
                document.querySelector('form').submit();
            } else {
                alert("결제에 실패하였습니다. 에러: " + rsp.error_msg);
            }
        },
    );
}
