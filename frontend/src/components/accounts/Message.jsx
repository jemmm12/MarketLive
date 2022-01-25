import { Link } from 'react-router-dom';

function Message() {
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
            <h1>받은 쪽지</h1>
        </div>
    )
}

export default Message;