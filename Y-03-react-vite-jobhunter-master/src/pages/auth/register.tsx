import { Button, Form, Input, Select, message, notification, Row, Col } from 'antd';
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { callRegister } from 'config/api';
import styles from 'styles/auth.module.scss';
import { IUser } from '@/types/backend';
import { ThunderboltFilled, ArrowLeftOutlined, UserOutlined, MailOutlined, LockOutlined, HomeOutlined } from '@ant-design/icons';
const { Option } = Select;

const RegisterPage = () => {
    const navigate = useNavigate();
    const [isSubmit, setIsSubmit] = useState(false);

    const onFinish = async (values: IUser) => {
        const { name, email, password, age, gender, address } = values;
        setIsSubmit(true);
        const res = await callRegister(name, email, password as string, +age, gender, address);
        setIsSubmit(false);
        if (res?.data?.id) {
            message.success('Đăng ký tài khoản thành công! Vui lòng đăng nhập.');
            navigate('/login');
        } else {
            notification.error({
                message: "Đăng ký thất bại",
                description:
                    res.message && Array.isArray(res.message) ? res.message[0] : res.message,
                duration: 5
            });
        }
    };

    return (
        <div className={styles["auth-page"]}>
            <div className={styles["auth-card"]} style={{ maxWidth: 540 }}>
                <div style={{ marginBottom: 16 }}>
                    <Link to="/" style={{ color: '#64748b', fontSize: 13, display: 'inline-flex', alignItems: 'center', gap: 6, textDecoration: 'none' }}>
                        <ArrowLeftOutlined /> Quay lại trang chủ
                    </Link>
                </div>

                <div className={styles["brand-header"]}>
                    <div className={styles["brand-badge"]}>
                        <ThunderboltFilled />
                    </div>
                    <h2 className={styles["title"]}>Tạo Tài Khoản Mới</h2>
                    <p className={styles["subtitle"]}>Bắt đầu hành trình bứt phá sự nghiệp công nghệ cùng DevHire</p>
                </div>

                <Form<IUser>
                    name="basic"
                    layout="vertical"
                    onFinish={onFinish}
                    autoComplete="off"
                    size="large"
                >
                    <Row gutter={12}>
                        <Col span={24} md={12}>
                            <Form.Item
                                label="Họ và tên"
                                name="name"
                                rules={[{ required: true, message: 'Vui lòng nhập họ tên!' }]}
                            >
                                <Input 
                                    prefix={<UserOutlined style={{ color: '#94a3b8' }} />} 
                                    placeholder="Nguyễn Văn A" 
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24} md={12}>
                            <Form.Item
                                label="Địa chỉ Email"
                                name="email"
                                rules={[
                                    { required: true, message: 'Vui lòng nhập email!' },
                                    { type: 'email', message: 'Email không hợp lệ!' }
                                ]}
                            >
                                <Input 
                                    prefix={<MailOutlined style={{ color: '#94a3b8' }} />} 
                                    placeholder="name@gmail.com" 
                                />
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row gutter={12}>
                        <Col span={24} md={12}>
                            <Form.Item
                                label="Mật khẩu"
                                name="password"
                                rules={[{ required: true, message: 'Vui lòng nhập mật khẩu!' }]}
                            >
                                <Input.Password 
                                    prefix={<LockOutlined style={{ color: '#94a3b8' }} />} 
                                    placeholder="••••••••" 
                                />
                            </Form.Item>
                        </Col>
                        <Col span={12} md={6}>
                            <Form.Item
                                label="Tuổi"
                                name="age"
                                rules={[{ required: true, message: 'Nhập tuổi!' }]}
                            >
                                <Input type='number' placeholder="22" />
                            </Form.Item>
                        </Col>
                        <Col span={12} md={6}>
                            <Form.Item
                                name="gender"
                                label="Giới tính"
                                rules={[{ required: true, message: 'Chọn giới tính!' }]}
                            >
                                <Select placeholder="Chọn" allowClear>
                                    <Option value="MALE">Nam</Option>
                                    <Option value="FEMALE">Nữ</Option>
                                    <Option value="OTHER">Khác</Option>
                                </Select>
                            </Form.Item>
                        </Col>
                    </Row>

                    <Form.Item
                        label="Địa chỉ (Tỉnh / Thành phố)"
                        name="address"
                        rules={[{ required: true, message: 'Vui lòng nhập địa chỉ!' }]}
                    >
                        <Input 
                            prefix={<HomeOutlined style={{ color: '#94a3b8' }} />} 
                            placeholder="Hà Nội, TP. Hồ Chí Minh, ..." 
                        />
                    </Form.Item>

                    <Form.Item style={{ marginTop: 20, marginBottom: 12 }}>
                        <Button 
                            type="primary" 
                            htmlType="submit" 
                            loading={isSubmit}
                            className={styles["auth-btn-primary"]}
                        >
                            Đăng Ký Tài Khoản
                        </Button>
                    </Form.Item>

                    <div className={styles["auth-footer"]}>
                        Đã có tài khoản DevHire? <Link to='/login'>Đăng nhập ngay</Link>
                    </div>
                </Form>
            </div>
        </div>
    );
};

export default RegisterPage;