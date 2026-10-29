import { useLocation, useNavigate, Link } from "react-router-dom";
import { useState, useEffect } from 'react';
import { ICompany } from "@/types/backend";
import { callFetchCompanyById } from "@/config/api";
import styles from 'styles/client.module.scss';
import parse from 'html-react-parser';
import { Col, Divider, Row, Skeleton, Breadcrumb } from "antd";
import { EnvironmentOutlined, HomeOutlined, BankOutlined, CheckCircleFilled, GlobalOutlined } from "@ant-design/icons";

const ClientCompanyDetailPage = (props: any) => {
    const [companyDetail, setCompanyDetail] = useState<ICompany | null>(null);
    const [isLoading, setIsLoading] = useState<boolean>(false);

    let location = useLocation();
    let params = new URLSearchParams(location.search);
    const id = params?.get("id"); // company id

    useEffect(() => {
        const init = async () => {
            if (id) {
                setIsLoading(true);
                const res = await callFetchCompanyById(id);
                if (res?.data) {
                    setCompanyDetail(res.data);
                }
                setIsLoading(false);
            }
        };
        init();
    }, [id]);

    return (
        <div className={`${styles["container"]} ${styles["detail-job-section"]}`}>
            <Breadcrumb
                style={{ marginBottom: 20 }}
                items={[
                    {
                        title: <Link to="/"><HomeOutlined /> Trang chủ</Link>,
                    },
                    {
                        title: <Link to="/company">Top Công ty IT</Link>,
                    },
                    {
                        title: companyDetail?.name || 'Chi tiết công ty',
                    },
                ]}
            />

            {isLoading ? (
                <div style={{ background: '#fff', padding: 30, borderRadius: 16 }}>
                    <Skeleton active avatar paragraph={{ rows: 6 }} />
                </div>
            ) : (
                <Row gutter={[24, 24]}>
                    {companyDetail && companyDetail.id && (
                        <>
                            <Col span={24} lg={16}>
                                <div className={styles["job-main-card"]}>
                                    <div style={{ display: 'flex', gap: 20, alignItems: 'center', marginBottom: 20 }}>
                                        <div style={{ 
                                            width: 80, 
                                            height: 80, 
                                            borderRadius: 14, 
                                            border: '1px solid #e2e8f0', 
                                            display: 'flex', 
                                            alignItems: 'center', 
                                            justifyContent: 'center', 
                                            padding: 6,
                                            background: '#fff' 
                                        }}>
                                            {companyDetail.logo ? (
                                                <img
                                                    style={{ maxWidth: '100%', maxHeight: '100%', objectFit: 'contain' }}
                                                    alt={companyDetail.name}
                                                    src={`${import.meta.env.VITE_BACKEND_URL}/storage/company/${companyDetail.logo}`}
                                                    onError={(e: any) => {
                                                        e.target.onerror = null;
                                                        e.target.src = 'https://placehold.co/100x100/4f46e5/ffffff?text=Company';
                                                    }}
                                                />
                                            ) : (
                                                <BankOutlined style={{ fontSize: 32, color: '#4f46e5' }} />
                                            )}
                                        </div>
                                        <div>
                                            <h1 className={styles["job-header-title"]} style={{ marginBottom: 6 }}>
                                                {companyDetail.name}
                                            </h1>
                                            <div style={{ color: '#64748b', fontSize: 14, display: 'flex', alignItems: 'center', gap: 6 }}>
                                                <EnvironmentOutlined style={{ color: '#0ea5e9' }} /> {companyDetail.address || 'Việt Nam'}
                                            </div>
                                        </div>
                                    </div>

                                    <Divider />

                                    <div className={styles["job-body-content"]}>
                                        <h3 style={{ fontSize: 18, fontWeight: 700, color: '#0f172a', marginBottom: 16 }}>
                                            Giới Thiệu Doanh Nghiệp
                                        </h3>
                                        {companyDetail.description ? parse(companyDetail.description) : (
                                            <p style={{ color: '#64748b' }}>Đang cập nhật thông tin chi tiết về doanh nghiệp.</p>
                                        )}
                                    </div>
                                </div>
                            </Col>

                            <Col span={24} lg={8}>
                                <div className={styles["company-sidebar-card"]}>
                                    <h3 style={{ fontSize: 16, fontWeight: 700, color: '#0f172a', marginBottom: 16, textAlign: 'left' }}>
                                        Thông Tin Doanh Nghiệp
                                    </h3>
                                    <div style={{ textAlign: 'left', display: 'flex', flexDirection: 'column', gap: 14, fontSize: 13.5 }}>
                                        <div>
                                            <span style={{ color: '#64748b', display: 'block' }}>Trạng thái:</span>
                                            <span style={{ color: '#10b981', fontWeight: 600, display: 'inline-flex', alignItems: 'center', gap: 4 }}>
                                                <CheckCircleFilled /> Đã xác thực
                                            </span>
                                        </div>
                                        <div>
                                            <span style={{ color: '#64748b', display: 'block' }}>Địa chỉ văn phòng:</span>
                                            <span style={{ color: '#0f172a', fontWeight: 500 }}>{companyDetail.address}</span>
                                        </div>
                                        <div>
                                            <span style={{ color: '#64748b', display: 'block' }}>Lĩnh vực công nghệ:</span>
                                            <span style={{ color: '#4f46e5', fontWeight: 600 }}>Phần mềm & Giải pháp IT</span>
                                        </div>
                                    </div>
                                </div>
                            </Col>
                        </>
                    )}
                </Row>
            )}
        </div>
    );
};

export default ClientCompanyDetailPage;