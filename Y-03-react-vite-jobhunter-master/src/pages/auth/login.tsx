import { Button, Divider, Form, Input, message, notification } from 'antd';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { callLogin } from 'config/api';
import { useState, useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { setUserLoginInfo } from '@/redux/slice/accountSlide';
import styles from 'styles/auth.module.scss';
import { useAppSelector } from '@/redux/hooks';
import { ThunderboltFilled, MailOutlined, LockOutlined, ArrowLeftOutlined } from '@ant-design/icons';

const LoginPage = () => {
    const navigate = useNavigate();
    const [isSubmit, setIsSubmit] = useState(false);
    const dispatch = useDispatch();
    const isAuthenticated = useAppSelector(state => state.account.isAuthenticated);

    let location = useLocation();
    let params = new URLSearchParams(location.search);
    const callback = params?.get("callback");

    useEffect(() => {
        if (isAuthenticated) {
            window.location.href = '/';
        }
    }, [isAuthenticated]);

    const onFinish = async (values: any) => {
        const { username, password } = values;
        setIsSubmit(true);
        const res = await callLogin(username, password);
        setIsSubmit(false);

        if (res?.data) {
            localStorage.setItem('access_token', res.data.access_token);
            dispatch(setUserLoginInfo(res.data.user));
            message.success('Đăng nhập thành công! Chào mừng trở lại.');
            window.location.href = callback ? callback : '/';
        } else {
            notification.error({
                message: "Đăng nhập thất bại",
                description:
                    res.message && Array.isArray(res.message) ? res.message[0] : res.message,
                duration: 5
            });
        }
    };

    return (
        <div className={styles["auth-page"]}>
            <div className={styles["auth-card"]}>
                <div style={{ marginBottom: 16 }}>
                    <Link to="/" style={{ color: '#64748b', fontSize: 13, display: 'inline-flex', alignItems: 'center', gap: 6, textDecoration: 'none' }}>
                        <ArrowLeftOutlined /> Quay lại trang chủ
                    </Link>
                </div>

                <div className={styles["brand-header"]}>
                    <div className={styles["brand-badge"]}>
                        <ThunderboltFilled />
                    </div>
                    <h2 className={styles["title"]}>Chào Mừng Trở Lại!</h2>
                    <p className={styles["subtitle"]}>Đăng nhập để kết nối với cơ hội việc làm IT tốt nhất</p>
                </div>

                <Form
                    name="basic"
                    layout="vertical"
                    onFinish={onFinish}
                    autoComplete="off"
                    size="large"
                >
                    <Form.Item
                        label="Địa chỉ Email"
                        name="username"
                        rules={[
                            { required: true, message: 'Vui lòng nhập Email!' },
                            { type: 'email', message: 'Email không đúng định dạng!' }
                        ]}
                    >
                        <Input 
                            prefix={<MailOutlined style={{ color: '#94a3b8', marginRight: 4 }} />} 
                            placeholder="name@company.com" 
                        />
                    </Form.Item>

                    <Form.Item
                        label="Mật khẩu"
                        name="password"
                        rules={[{ required: true, message: 'Vui lòng nhập mật khẩu!' }]}
                    >
                        <Input.Password 
                            prefix={<LockOutlined style={{ color: '#94a3b8', marginRight: 4 }} />} 
                            placeholder="••••••••" 
                        />
                    </Form.Item>

                    <Form.Item style={{ marginTop: 24, marginBottom: 12 }}>
                        <Button 
                            type="primary" 
                            htmlType="submit" 
                            loading={isSubmit}
                            className={styles["auth-btn-primary"]}
                        >
                            Đăng Nhập
                        </Button>
                    </Form.Item>

                    <div className={styles["auth-footer"]}>
                        Chưa có tài khoản DevHire? <Link to='/register'>Đăng ký ngay</Link>
                    </div>
                </Form>
            </div>
        </div>
    );
};

export default LoginPage;