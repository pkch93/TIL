# 2021.05.25 TIL - React Native Dynamic Image 에러 처리

Dynamic Image 에러 처리

props로 path를 전달하여 Image 컴포넌트의 source에 require을 사용하는 경우 500 error가 발생한다.

```text
import React from 'react'
import styled from 'styled-components/native'

const PictureFrame = styled.Image`
`

interface PictureFrameProps {
    imagePath: string,
}

export default ({ imagePath }: PictureFrameProps) => (
    <PictureFrame source={require(${imagePath})}/>
)
```

```text
import React from 'react'
import styled from 'styled-components/native'

import PictureFrame from '@/components/PictureFrame'

const Wrapper = styled.View`
`

export default () => (
    <Wrapper>
        <PictureFrame imagePath='@/assets/img/picture.png' />
    </Wrapper>
)
```

위와 같이 source로 이미지 경로를 받아 jsx 내에서 require를 하는 경우 500 error가 발생한다.

이를 해결하기 위해서는 미리 require로 이미지 소스를 받은 뒤에 props로 전달하면된다. 참고로 source 이미지의 타입은 ImageSourcePropType이다.

```text
import React from 'react'
import { ImageSourcePropType } from 'react-native'
import styled from 'styled-components/native'

const PictureFrame = styled.Image`
`

interface PictureFrameProps {
    imagePath: ImageSourcePropType,
}

export default ({ source }: PictureFrameProps) => (
    <PictureFrame source={source)/>
)
```

```text
import React from 'react'
import styled from 'styled-components/native'

import PictureFrame from '@/components/PictureFrame'

const Wrapper = styled.View`
`

export default () => (
    <Wrapper>
        <PictureFrame source={require('@/assets/img/picture.png')} />
    </Wrapper>
)
```

위와 같이 전달할때 ImageSourcePropType로 전달하면 에러가 해결된다.

참고: [https://stackoverflow.com/questions/30854232/react-native-image-require-module-using-dynamic-names](https://stackoverflow.com/questions/30854232/react-native-image-require-module-using-dynamic-names)

