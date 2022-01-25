import { Link, useParams } from 'react-router-dom';
import { useState } from 'react';

function MessageWrite() {
    
    // 동적 라우팅 저장
    const {to_nickname} = useParams()


    // 내용 저장
    const [inputs, setInputs] = useState({
        to: to_nickname,
        title: '',
        content: '',
    });
    const { to, title, content } = inputs; 
    const onChange = (e) => {
        const { value, name } = e.target;
        setInputs({
            ...inputs, 
            [name]: value 
        });
    };


    // 보내기 버튼 누를시
    const onSend = ()  => {
        console.log(inputs)
    }

    return(
        <div>
            <ul>
                <li>
                    <Link to="/message">받은 쪽지</Link>
                </li>
                <li>
                    <Link to="/message/write">쪽지 쓰기</Link>
                </li>
            </ul>
            <h1>쪽지 쓰기</h1>
            <div>
                <div>
                    받는 사람: 
                    <input name="to"
                    type="text" 
                    onChange={onChange}
                    value={to}
                    maxLength="15"
                    />
                </div>
                <div>
                    제목: 
                    <input name="title"
                    type="text" 
                    onChange={onChange}
                    value={title}
                    maxLength="30"
                    />
                </div>
                <div>
                    <textarea name="content" 
                    id="" cols="30" rows="10"
                    onChange={onChange}
                    value={content}
                    maxLength="100"
                    ></textarea>
                </div>
                <button onClick={onSend}>보내기</button>
            </div>
        </div>
    )
}

export default MessageWrite;