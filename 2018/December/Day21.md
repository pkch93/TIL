# 2018 12.21 Friday - pandas groupby

# 학습내용

1. 데이터 사이언스 by python

- pandas groupby로 다양한 데이터 핸들링

    - 시간과 데이터 종류가 정리된 통화량 데이터
    
    https://www.sanelynn.ie/wp-content/uploads/2015/06/phone_data.csv

    ※ dateutil
    parser의 한 종류로 python에서 date 정보를 편한 형태로 변경하도록 도와준다.

    ```python
    # 월별로
    df.groupby("month")["duration"].sum()

    # item이 call인 경우
    df[df["item"] == "call"].groupby("month")["duration"].sum()

    # 월별로 item의 건수를 새는 경우
    df.groupby(["month", "item"])["date"].count()

    # agg 함수 이용하여 월별 duration 계산
    # 이때, agg 함수의 인자는 dict 형태가 들어가는데 key는 기준이 되는 column 값, value는 연산 함수명을 입력한다.
    # as_index=False는 index를 붙이지 말라는 의미
    df.groupby("month", as_index=False).agg({"duration": "sum"})
    
    # 1개 이상
    df.groupby(["month", "item"]).agg({"duration": "sum", "network_type": "count", "date": "first"})

    # 1개의 key값에 여러 함수 적용
    # value에 list로 연산 함수를 전달
    df.groupby(["month", "item"]).agg({"duration": [sum, min, max], "network_type": "count", "date": "first"})
    
    # column 이름 뽑아내기
    df.columns.droplevel(level=0)

    # column 이름 바꾸기, rename / add_prifix
    df.rename(columns={"min": "min_duration", "max": "max_duration"})
    df.add_prefix("duration_")
    ```
