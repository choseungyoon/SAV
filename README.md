# SAV-Search-App-by-Voice-
서강대학교 대화형사용자 인터페이스  PROJECT

#DETAIL
사용자가 음성으로 어플리케이션 이름을 입력하면, 해당 어플리케이션을 설치하였는지 확인하고, 
설치가 되어있으면 해당 앱을 실행한다.
만약 설치되어 있지 않은 어플리케이션이면, Google Playstore를 통해 해당 앱을 검색하도록 한다.

#OBJECT
- 음성인식을 통해 사용자에게 어플리케이션 이름을 입력 받는 단어음성인식기를 개발한다.
- 사용자에게 입력받은 단어와 일치하는 어플리케이션을 찾아 실행권한이 있을 경우 실행시킨다. 
실행권한이란 해당 앱이 다른 앱과 연동할 수 있는지 없는지 유무이다.
- 실행권한이 없을 경우, 팝업창을 통해 존재여부를 알려준다.
- 같은 이름이 포함된 어플리케이션이 여러 개 존재하는 경우, 목록을 보여주어 선택 시 실행권한이 있을 경우 실행시킨다.
- 존재하는 어플리케이션이 없는 경우, Goole playstore를 통하여 해당 앱을 검색한다.

#ENVIRONMENT
- 개발환경 : Android 5.1 Mashmellow
- 사용언어 : Java
- 사용 tool : Android studio
- 사용 API : Naver 음성인식 SDK (https://developers.naver.com/products/vrecog)

#CONTRIBUTER
- 서강대학교 컴퓨터공학과 조승윤  
- 서강대학교 컴퓨터공학과 김정인  
