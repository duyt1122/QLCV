import { Card, Col, Row, Statistic, Progress } from "antd";
import CountUp from 'react-countup';
import { 
    UserOutlined, 
    ScheduleOutlined, 
    BankOutlined, 
    FileTextOutlined,
    ArrowUpOutlined,
    ThunderboltFilled,
    CheckCircleFilled
} from '@ant-design/icons';

const DashboardPage = () => {
    const formatter = (value: number | string) => {
        return (
            <CountUp end={Number(value)} separator="," />
        );
    };

    return (
        <div>
            {/* Page Header */}
            <div style={{ marginBottom: 24 }}>
                <h1 style={{ fontSize: 24, fontWeight: 800, color: '#0f172a', marginBottom: 4 }}>
                    Bảng Điều Khiển Trung Tâm
                </h1>
                <p style={{ color: '#64748b', fontSize: 14 }}>
                    Theo dõi số liệu và hoạt động tuyển dụng trên toàn hệ thống DevHire
                </p>
            </div>

            {/* Statistics Row */}
            <Row gutter={[20, 20]}>
                {/* Active Users */}
                <Col span={24} sm={12} lg={6}>
                    <Card 
                        bordered={false} 
                        style={{ 
                            borderRadius: 16, 
                            boxShadow: '0 4px 16px rgba(0,0,0,0.03)',
                            border: '1px solid #e2e8f0'
                        }}
                    >
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                            <div>
                                <span style={{ color: '#64748b', fontSize: 13, fontWeight: 600 }}>NGƯỜI DÙNG</span>
                                <div style={{ fontSize: 28, fontWeight: 800, color: '#0f172a', marginTop: 4 }}>
                                    <Statistic value={12540} formatter={formatter} valueStyle={{ fontWeight: 800, fontSize: 28, color: '#0f172a' }} />
                                </div>
                            </div>
                            <div style={{ 
                                width: 44, 
                                height: 44, 
                                borderRadius: 12, 
                                background: 'rgba(79, 70, 229, 0.1)', 
                                color: '#4f46e5',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                fontSize: 20
                            }}>
                                <UserOutlined />
                            </div>
                        </div>
                        <div style={{ marginTop: 12, fontSize: 12.5, color: '#10b981', display: 'flex', alignItems: 'center', gap: 4, fontWeight: 600 }}>
                            <ArrowUpOutlined /> +14.2% <span style={{ color: '#94a3b8', fontWeight: 400 }}>so với tháng trước</span>
                        </div>
                    </Card>
                </Col>

                {/* Total Jobs */}
                <Col span={24} sm={12} lg={6}>
                    <Card 
                        bordered={false} 
                        style={{ 
                            borderRadius: 16, 
                            boxShadow: '0 4px 16px rgba(0,0,0,0.03)',
                            border: '1px solid #e2e8f0'
                        }}
                    >
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                            <div>
                                <span style={{ color: '#64748b', fontSize: 13, fontWeight: 600 }}>VIỆC LÀM ĐANG MỞ</span>
                                <div style={{ fontSize: 28, fontWeight: 800, color: '#0f172a', marginTop: 4 }}>
                                    <Statistic value={2480} formatter={formatter} valueStyle={{ fontWeight: 800, fontSize: 28, color: '#0f172a' }} />
                                </div>
                            </div>
                            <div style={{ 
                                width: 44, 
                                height: 44, 
                                borderRadius: 12, 
                                background: 'rgba(16, 185, 129, 0.1)', 
                                color: '#10b981',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                fontSize: 20
                            }}>
                                <ScheduleOutlined />
                            </div>
                        </div>
                        <div style={{ marginTop: 12, fontSize: 12.5, color: '#10b981', display: 'flex', alignItems: 'center', gap: 4, fontWeight: 600 }}>
                            <ArrowUpOutlined /> +8.5% <span style={{ color: '#94a3b8', fontWeight: 400 }}>so với tháng trước</span>
                        </div>
                    </Card>
                </Col>

                {/* Companies */}
                <Col span={24} sm={12} lg={6}>
                    <Card 
                        bordered={false} 
                        style={{ 
                            borderRadius: 16, 
                            boxShadow: '0 4px 16px rgba(0,0,0,0.03)',
                            border: '1px solid #e2e8f0'
                        }}
                    >
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                            <div>
                                <span style={{ color: '#64748b', fontSize: 13, fontWeight: 600 }}>DOANH NGHIỆP</span>
                                <div style={{ fontSize: 28, fontWeight: 800, color: '#0f172a', marginTop: 4 }}>
                                    <Statistic value={680} formatter={formatter} valueStyle={{ fontWeight: 800, fontSize: 28, color: '#0f172a' }} />
                                </div>
                            </div>
                            <div style={{ 
                                width: 44, 
                                height: 44, 
                                borderRadius: 12, 
                                background: 'rgba(245, 158, 11, 0.1)', 
                                color: '#f59e0b',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                fontSize: 20
                            }}>
                                <BankOutlined />
                            </div>
                        </div>
                        <div style={{ marginTop: 12, fontSize: 12.5, color: '#10b981', display: 'flex', alignItems: 'center', gap: 4, fontWeight: 600 }}>
                            <ArrowUpOutlined /> +18.7% <span style={{ color: '#94a3b8', fontWeight: 400 }}>so với tháng trước</span>
                        </div>
                    </Card>
                </Col>

                {/* Resumes */}
                <Col span={24} sm={12} lg={6}>
                    <Card 
                        bordered={false} 
                        style={{ 
                            borderRadius: 16, 
                            boxShadow: '0 4px 16px rgba(0,0,0,0.03)',
                            border: '1px solid #e2e8f0'
                        }}
                    >
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                            <div>
                                <span style={{ color: '#64748b', fontSize: 13, fontWeight: 600 }}>CV ĐÃ NỘP</span>
                                <div style={{ fontSize: 28, fontWeight: 800, color: '#0f172a', marginTop: 4 }}>
                                    <Statistic value={35290} formatter={formatter} valueStyle={{ fontWeight: 800, fontSize: 28, color: '#0f172a' }} />
                                </div>
                            </div>
                            <div style={{ 
                                width: 44, 
                                height: 44, 
                                borderRadius: 12, 
                                background: 'rgba(6, 182, 212, 0.1)', 
                                color: '#06b6d4',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                fontSize: 20
                            }}>
                                <FileTextOutlined />
                            </div>
                        </div>
                        <div style={{ marginTop: 12, fontSize: 12.5, color: '#10b981', display: 'flex', alignItems: 'center', gap: 4, fontWeight: 600 }}>
                            <ArrowUpOutlined /> +24.1% <span style={{ color: '#94a3b8', fontWeight: 400 }}>so với tháng trước</span>
                        </div>
                    </Card>
                </Col>
            </Row>

            {/* Quick Status / Health Row */}
            <Row gutter={[20, 20]} style={{ marginTop: 20 }}>
                <Col span={24} md={16}>
                    <Card 
                        title={<span style={{ fontWeight: 700, fontSize: 16 }}>Tiến Độ Xử Lý Hồ Sơ Ứng Tuyển</span>}
                        style={{ borderRadius: 16, border: '1px solid #e2e8f0' }}
                    >
                        <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
                            <div>
                                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 6, fontSize: 13.5 }}>
                                    <span style={{ fontWeight: 600, color: '#0f172a' }}>Hồ sơ đã xem xét (Screening)</span>
                                    <span style={{ color: '#4f46e5', fontWeight: 700 }}>86%</span>
                                </div>
                                <Progress percent={86} strokeColor="#4f46e5" showInfo={false} />
                            </div>
                            <div>
                                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 6, fontSize: 13.5 }}>
                                    <span style={{ fontWeight: 600, color: '#0f172a' }}>Mời phỏng vấn thành công (Interview)</span>
                                    <span style={{ color: '#10b981', fontWeight: 700 }}>64%</span>
                                </div>
                                <Progress percent={64} strokeColor="#10b981" showInfo={false} />
                            </div>
                            <div>
                                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 6, fontSize: 13.5 }}>
                                    <span style={{ fontWeight: 600, color: '#0f172a' }}>Nhận việc thành công (Hired)</span>
                                    <span style={{ color: '#06b6d4', fontWeight: 700 }}>42%</span>
                                </div>
                                <Progress percent={42} strokeColor="#06b6d4" showInfo={false} />
                            </div>
                        </div>
                    </Card>
                </Col>

                <Col span={24} md={8}>
                    <Card 
                        title={<span style={{ fontWeight: 700, fontSize: 16 }}>Trạng Thái Hệ Thống</span>}
                        style={{ borderRadius: 16, border: '1px solid #e2e8f0' }}
                    >
                        <div style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
                            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                                <span style={{ color: '#64748b' }}>Hệ thống API Backend:</span>
                                <span style={{ color: '#10b981', fontWeight: 600, display: 'inline-flex', alignItems: 'center', gap: 4 }}>
                                    <CheckCircleFilled /> Hoạt động tốt
                                </span>
                            </div>
                            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                                <span style={{ color: '#64748b' }}>Dịch vụ tải tệp CV:</span>
                                <span style={{ color: '#10b981', fontWeight: 600, display: 'inline-flex', alignItems: 'center', gap: 4 }}>
                                    <CheckCircleFilled /> Sẵn sàng
                                </span>
                            </div>
                            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                                <span style={{ color: '#64748b' }}>Phiên bản Frontend:</span>
                                <span style={{ color: '#4f46e5', fontWeight: 700 }}>DevHire v2.0 Pro</span>
                            </div>
                        </div>
                    </Card>
                </Col>
            </Row>
        </div>
    );
};

export default DashboardPage;