# 2018 12.24 Monday - Spring Securty Overview, pandas (결축치 처리, 이산값 처리, Feature Scaling)

## 1. Spring REST API

- Spring Security OAuth2

    > ※ Spring Security
    >
    > Spring Security는 크게 2가지로 나눌 수 있다.
    >
    > 웹 요청 보안을 담당하는 웹 시큐리티 / 메서드 시큐리티
    >
    > 둘 다 Security Interceptor를 사용한다.
    >
    > 웹 시큐리티는 Filter 기반 / 메서드 시큐리티는 AOP와 같이 Proxy를 추가하여 보안

    간략한 동작 흐름

    요청 -> 요청을 Servlet Filter가 가로챔 -> Security Interceptor로 요청을 전달 -> 요청을 보고 Security 적용 판단 -> 필요하다면 Interceptor로 요청이 전달 -> 인증된 사용자가 있다면 SecurityContextHolder에서 인증 정보를 가져옴 / 아니라면 인증을 시도 -> AuthenticationManager가 로그인을 담당하여 인증이 되면 SecurityContextHolder에 저장한다. -> 그 후 AccessDecisionManager로 권한을 판단한다.

## 2. 데이터 사이언스 by python

- 결측치가 있을 때 (Missing Values)

    + 데이터가 없으면 sample을 drop
    + 데이터가 없는 최소 개수를 정하여 sample을 drop
    + 데이터가 거의 없는 feature는 feature 자체를 drop

    ```python
    df.isnull().sum() # NaN 값이 얼마나 있는지 확인하는 함수
    df.dropna() # NaN 값을 지우는 방법
    df.dropna(how="all") # 모든 데이터가 비어 있으면 drop
    df.dropna(axis=1, thresh=3) # column을 기준으로 데이터가 최소 3개 이하라면 drop
    ```
    + 최빈값, 평균값으로 비어있는 데이터 채우기

    > 평균값 : 해당 column의 값들의 평균
    >
    > 중위값 : 값을 일렬로 나열했을 때 중간에 위치한 값
    >
    > 최빈값 : 가장 많이 나오는 값

    이들 값의 분포를 확인하고 적절한 값을 채우는 것이 중요!

    ```python
    avg = df["score"].mean() # 평균값
    df["score"].median() # 중위값
    df["score"].mode() # 최빈값

    df["score"].fillna(avg, inplace=True) # score column의 NaN값을 평균값으로 채우기

    df["score"].fillna(df.groupby("sex")["score"].transform("mean"), inplace=True)
    # 성별로 나누어 평균값을 집어 넣는 방법
    df[df["age"].notnull() & df["sex"].notnull()]
    # Age와 Sex가 모두 NaN이 아닌 경우를 뽑아오는 방법
    ```
    
- Categorital data 처리하기

    {남자, 여자}, {빨간색, 파란색, 노란색} 등 이산형 데이터를 처리하는 방법

    > 일반적으로 One-Hot Encoding으로 처리한다.
    >
    > 실제 데이터 set의 크기만큼 **binary feature**를 생성
    
    ```python
    edges = pd.DataFrame({
        "source": [0,1,2],
        "target": [2,2,3],
        "weight": [3,4,5],
        "color" : ["red", "blue", "blue"]
    })
 
    pd.get_dummies(edges) # 이산형 데이터를 숫자로 바꾸는 방법
    # binary가 아닌 object 타입의 값을 바꿔준다.

    pd.get_dummies(edges["color"]) # Series로
    pd.get_dummies(edges[["color"]]) # DataFrame으로

    weight_dict = {3: "M", 4: "L", 5:" XL"}
    edges["weight_sign"] = edges["weight"].map(weight_dict)
    # binary 데이터를 이산형 테이터로 바꾸는 방법
    ```

    또한 데이터의 구간을 나누어야하는 경우도 있다. (Data Binding)

    > 키, 몸무게 등 구간을 나누어 데이터를 넣어야 하는 경우

    ```python
    binds = [0, 25, 50, 75, 100] # 구간을 정의
    group_names = ["Low", "Okay", "Good", "Great"]
    categories = pd.cut(df["score"], binds, labels=group_names)
    ```

    pandas의 **cut** 함수를 이용하여 score 값을 기준으로 binds에 정의한 구간에 해당하는 label을 붙여준다.


    ※ sklearn으로 Label encoding하기

    pandas 뿐만 아니라 Scikit-learn (sklearn)의 preprocessing 패키지도 label, one-hot을 지원해준다.

    ```python
    from sklearn import preprocessing
    le = preprocessing.LabelEncoder() # label Encoder 생성
    le.fit(raw_ex[:, 0]) # 해당 데이터에 대한 encoding fitting
    le.transform(raw_ex[:, 0]) # 실제 데이터를 labeling
    ```

    위에 Label Encoder에서 fit과 transform 과정이 나눠진 이유는 새로운 데이터 입력시 **기존의 labeling 규칙을 그대로 적용**할 필요가 있기 때문이다.

    **fit**은 규칙을 생성, **transform**은 규칙을 적용하는 과정이다. fit을 통해 규칙이 생성된 **labelencoder는 따로 저장**하여 새로운 데이터를 입력할 경우 사용할 수 있다.

    ```python
    # 사용예시 (기존 데이터를 전처리)
    label_column = [0,1,2,5]
    label_encoder_list = []
    for column_index in label_column:
        le = preprocessing.LabelEncoder()
        le.fit(raw_ex[:, column_index]) # 규칙 생성
        data[:, column_index] = le.transform(raw_ex[:, column_index]) # 규칙 적용
        label_encoder_list.append(le) # 사용한 labelEncoder 저장
        del le # 저장 후 삭제
    
    # 기존 데이터에 새로운 데이터가 나오는 경우, 적용
    label_encoder_list[0].transform(raw_ex[:10, 0])
    ```

    이렇게 Numeric labeling이 완료된 데이터에 one-hot encoding을 적용할 수 있다. (데이터는 1-dim, 일차원으로 변환하여 넣을 것)

    ```python
    one_hot_enc = preprocessing.OneHotEncoder()
    one_hot_enc.fit(data[:, 0].reshape(-1, 1)) # 일차원으로 변환하여 fit

    onehotlabels = one_hot_enc.transform(data[:, 0].reshape(-1, 1)).toarray()
    ```

- Feature Scaling

    두 변수 중 하나의 값이 너무 클 경우! 데이터를 조절하는 방법
    (Feature 간의 최대-최소값 차이를 맞추기)

    크게 2가지 전략이 있다.

    - Min-Max Normalization (정규화)

        기존 변수의 범위를 새로운 최대-최소로 변경하기 (일반적으로 0과 1 사이)

        ```python
        # pandas
        (df["A"] - df["A"].min()) / (df["A"].max() - df["A"].min()) * (5 - 1) + 1
        ```

    - Standardization (Z-score Normalization)

        기존 변수에 범위를 정규 분포로 변환 (실제 Min-Max 값을 모를 때 활용 가능)


        ```python
        # pandas
        df["B"] = (df["B"] - df["B"].mean()) / df["B"].std()
        ```

    > 주의사항!
    >
    > 실제 사용할 때는 반드시 정규화 Parameter (최대 / 최소, 평균 / 표준편차) 등을 기억하여 새로운 값에 적용해야한다.

    ```python
    # sklearn, 반드시 fit으로 규칙을 기억한 후 transform으로 적용해야한다.
    std_scale = preprocessing.StandardScaler().fit(df[["Alcohol", "Malic acid"]])
    df_std = std_scale.transform(df[["Alcohol", "Malic acid"]])

    minmax_scale = preprocessing.MinMaxScaler(feature_range=(0, 5)).fit(df[["Alcohol", "Malic acid"]])
    df_minmax = minmax_scale.transform(df[["Alcohol", "Malic acid"]])
    ```

    > feature_range는 변환되는 값의 범위를 지정하는 인자이다.
