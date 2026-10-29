import styles from 'styles/client.module.scss';
import SearchClient from '@/components/client/search.client';
import JobCard from '@/components/client/card/job.card';
import CompanyCard from '@/components/client/card/company.card';
import { 
    RocketOutlined, 
    SafetyCertificateOutlined, 
    ThunderboltOutlined, 
    CrownOutlined,
    CheckCircleFilled
} from '@ant-design/icons';
import { Row, Col, Card } from 'antd';

const HomePage = () => {
    return (
        <div>
            {/* HERO BANNER SECTION */}
            <section className={styles["hero-section"]}>
                <div className={styles["container"]}>
                    <div style={{ textAlign: 'center', maxWidth: 840, margin: '0 auto' }}>
                        <div className={styles["hero-badge"]}>
                            <RocketOutlined /> Nền tảng tuyển dụng công nghệ thế hệ mới
                        </div>
                        <h1 className={styles["hero-title"]}>
                            Khám Phá Cơ Hội Nghề Nghiệp <span>IT Đỉnh Cao</span>
                        </h1>
                        <p className={styles["hero-subtitle"]} style={{ margin: '0 auto 28px' }}>
                            Hơn 2,500+ vị trí tuyển dụng Tech chất lượng từ các kỳ lân công nghệ, tập đoàn đa quốc gia và startup hàng đầu.
                        </p>
                    </div>

                    {/* HERO FLOATING SEARCH BOX */}
                    <div className={styles["hero-search-box"]}>
                        <SearchClient />
                    </div>

                    {/* PLATFORM STATS */}
                    <div className={styles["hero-stats"]}>
                        <div className={styles["stat-card"]}>
                            <span className={styles["stat-number"]}>2,500+</span>
                            <span className={styles["stat-label"]}>Việc làm IT đang tuyển</span>
                        </div>
                        <div className={styles["stat-card"]}>
                            <span className={styles["stat-number"]}>650+</span>
                            <span className={styles["stat-label"]}>Doanh nghiệp hàng đầu</span>
                        </div>
                        <div className={styles["stat-card"]}>
                            <span className={styles["stat-number"]}>98%</span>
                            <span className={styles["stat-label"]}>Phản hồi trong 48 giờ</span>
                        </div>
                        <div className={styles["stat-card"]}>
                            <span className={styles["stat-number"]}>150,000+</span>
                            <span className={styles["stat-label"]}>Lập trình viên tin dùng</span>
                        </div>
                    </div>
                </div>
            </section>

            {/* MAIN CONTENT CONTAINER */}
            <div className={styles["container"]}>
                {/* FEATURED COMPANIES */}
                <CompanyCard />

                <div style={{ margin: '40px 0' }}></div>

                {/* LATEST JOBS */}
                <JobCard />

                <div style={{ margin: '50px 0' }}></div>

                {/* WHY CHOOSE DEVHIRE / VALUE PROPOSITION */}
                <div style={{ marginBottom: 40 }}>
                    <div style={{ textAlign: 'center', marginBottom: 30 }}>
                        <span style={{ color: '#4f46e5', fontWeight: 700, fontSize: 13, textTransform: 'uppercase', letterSpacing: 1 }}>
                            TẠI SAO CHỌN CHÚNG TÔI
                        </span>
                        <h2 style={{ fontSize: 28, fontWeight: 800, color: '#0f172a', marginTop: 6 }}>
                            Đồng Hành Cùng Sự Nghiệp Của Bạn
                        </h2>
                    </div>

                    <Row gutter={[20, 20]}>
                        <Col span={24} md={8}>
                            <Card 
                                style={{ 
                                    borderRadius: 16, 
                                    border: '1px solid #e2e8f0', 
                                    height: '100%',
                                    boxShadow: '0 4px 14px rgba(0,0,0,0.03)'
                                }}
                            >
                                <div style={{ 
                                    width: 48, 
                                    height: 48, 
                                    borderRadius: 12, 
                                    background: 'rgba(79, 70, 229, 0.1)', 
                                    color: '#4f46e5',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    fontSize: 22,
                                    marginBottom: 16
                                }}>
                                    <CrownOutlined />
                                </div>
                                <h3 style={{ fontSize: 18, fontWeight: 700, color: '#0f172a', marginBottom: 8 }}>
                                    Việc Làm Chất Lượng Cao
                                </h3>
                                <p style={{ color: '#64748b', fontSize: 14, lineHeight: 1.6 }}>
                                    100% tin tuyển dụng được kiểm duyệt minh bạch về mức lương, chế độ đãi ngộ và công nghệ thực tế.
                                </p>
                            </Card>
                        </Col>
                        <Col span={24} md={8}>
                            <Card 
                                style={{ 
                                    borderRadius: 16, 
                                    border: '1px solid #e2e8f0', 
                                    height: '100%',
                                    boxShadow: '0 4px 14px rgba(0,0,0,0.03)'
                                }}
                            >
                                <div style={{ 
                                    width: 48, 
                                    height: 48, 
                                    borderRadius: 12, 
                                    background: 'rgba(16, 185, 129, 0.1)', 
                                    color: '#10b981',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    fontSize: 22,
                                    marginBottom: 16
                                }}>
                                    <ThunderboltOutlined />
                                </div>
                                <h3 style={{ fontSize: 18, fontWeight: 700, color: '#0f172a', marginBottom: 8 }}>
                                    Ứng Tuyển Nhanh Chóng
                                </h3>
                                <p style={{ color: '#64748b', fontSize: 14, lineHeight: 1.6 }}>
                                    Nộp CV trực tiếp tới nhà tuyển dụng chỉ với 1 click. Theo dõi trạng thái hồ sơ theo thời gian thực.
                                </p>
                            </Card>
                        </Col>
                        <Col span={24} md={8}>
                            <Card 
                                style={{ 
                                    borderRadius: 16, 
                                    border: '1px solid #e2e8f0', 
                                    height: '100%',
                                    boxShadow: '0 4px 14px rgba(0,0,0,0.03)'
                                }}
                            >
                                <div style={{ 
                                    width: 48, 
                                    height: 48, 
                                    borderRadius: 12, 
                                    background: 'rgba(6, 182, 212, 0.1)', 
                                    color: '#06b6d4',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    fontSize: 22,
                                    marginBottom: 16
                                }}>
                                    <SafetyCertificateOutlined />
                                </div>
                                <h3 style={{ fontSize: 18, fontWeight: 700, color: '#0f172a', marginBottom: 8 }}>
                                    Bảo Mật Thông Tin
                                </h3>
                                <p style={{ color: '#64748b', fontSize: 14, lineHeight: 1.6 }}>
                                    Hồ sơ cá nhân và quyền riêng tư của ứng viên được bảo vệ tuyệt đối theo tiêu chuẩn quốc tế.
                                </p>
                            </Card>
                        </Col>
                    </Row>
                </div>
            </div>
        </div>
    );
};

export default HomePage;