## ๐ git ํด๋ ๊ตฌ์กฐ   



- frontend - Vue.js
- backend - Spring Boot
- analy - ํ์ด์ฌ ๋ฐ์ดํฐ ๋ถ์
- document - ๋ฌธ์ ์์   


  <br>
    
    
## ๐  git ๊ท์น



- Commit๊ท์น

    -์ฐธ๊ณ  ๋งํฌ : [https://udacity.github.io/git-styleguide/](https://udacity.github.io/git-styleguide/)

    - **feat:**ย A new feature

        : ์๋ก์ด ๊ธฐ๋ฅ ๊ตฌํ

    - **fix:**ย A bug fix

        : ๋ฒ๊ทธ ํฝ์ค, ๊ธฐํ ๊ธฐ๋ฅ ์์ 

    - **docs:**ย Changes to documentation

        : ๋ฌธ์ ๋ณ๊ฒฝ ์ ( readme ๊ฐ์ ๊ฒ ๋ณ๊ฒฝ์)

    - **style:**ย Formatting, missing semi colons, etc; no code change

        : ์ฝ๋ ๋ณ๊ฒฝ ์ฌํญ์์ด ์์ํ ํฌ๋งท ๋ณ๊ฒฝ ์: ์๋ฅผ ๋ค์ด ์์๋ฐ์ดํ์์ ํฐ ๋ฐ์ดํ๋ก ๋ณ๊ฒฝ

    - **refactor:**ย Refactoring production code

        : ์ด๋ฏธ ์๋ ์ฝ๋๋ฅผ ๋ฆฌํฉํ ๋งํ ๋ (๊น๋ํ๊ฒ ์์ )

    - **test:**ย Adding tests, refactoring test; no production code change

        : ํ์คํธ ์ฝ๋ ์ถ๊ฐ ์

    - **chore:**ย Updating build tasks, package manager configs, etc; no production code change

        : ์ก์ผ , ์์ฐ์ฑ ์๋ ์ฝ๋ ๋ณ๊ฒฝ ์, ์ ๋ชจ๋  ๊ฒ์ ํด๋น์ฌํญ์์ ์ ์ฌ๊ธฐ๋ก


<br>


## ๐ git commit ์์


   ```
ex) [backend] feat : ๋ก๊ทธ์ธ๊ธฐ๋ฅ

    feat

    fix

    docs

    refactor

    test
   ```


<br>


## ๐ฅข branch์ ๋ต ์์


```
master

  develop

    analy

    backend

        B/user

        B/wine

        B/reco

    frontend

        (App.vue)

        F/nav

        F/guideBtn

        (Router-link)

        F/main

        F/recommendation

        F/wines

        F/community

        F/topic

        F/myInfo

        F/wineDetails

        F/tasteSet

        F/makingTaste

        (modal)

        F/login

        F/join
```

<br>


## ๐push ์์


```
๋ฐฐํฌ๋ develop pushํ  ๊ฒฝ์ฐ๋ง (์  ํจ์ค ์ฌ์ฉํด์)
```
### [backend]

1. backend ํ์ ๊ธฐ๋ฅ๋ณ ๋ธ๋ฐ์น์์ ์์
2. local์์ ํ์คํธ โ ์ ๋๋ก ๋์๊ฐ ๋

    2-1. git push origin B/reco

    2-1. backend๋ก B/reco๋ฅผ merge

    2-3. git push origin backend

### [frontend]

1. frontend ํ์ ๊ธฐ๋ฅ๋ณ ๋ธ๋ฐ์น์์ ์์
2. ํ์คํธ

    2-1. ํด๋ก  ๋ฐฑ์๋

    2-2. local์์ ํ์คํธ โ ์ ๋๋ก ๋์๊ฐ ๋

    2-3. git push origin F/reco

    2-4. frontend๋ก F/reco๋ฅผ merge

    2-5. git push origin frontend