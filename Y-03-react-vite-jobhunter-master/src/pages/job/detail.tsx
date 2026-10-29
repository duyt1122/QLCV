import { useLocation, useNavigate, Link } from "react-router-dom";
import { useState, useEffect } from 'react';
import { IJob } from "@/types/backend";
import { callFetchJobById } from "@/config/api";
import styles from 'styles/client.module.scss';
import parse from 'html-react-parser';
import { Col, Divider, Row, Skeleton, Tag, Breadcrumb, Button } from "antd";
import { 
    DollarCircleFilled, 
    EnvironmentOutlined, 
    ClockCircleOutlined,
    RocketOutlined,
    BankOutlined,
    CheckCircleFilled,
    HomeOutlined,
    ShareAltOutlined,
    HeartOutlined
} from "@ant-design/icons";
import { convertSlug, getLocationName } from "@/config/utils";
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import ApplyModal from "@/components/client/modal/apply.modal";
dayjs.extend(relativeTime);

const ClientJobDetailPage = (props: any) => {
    const [jobDetail, setJobDetail] = useState<IJob | null>(null);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const navigate = useNavigate();

    let location = useLocation();
    let params = new URLSearchParams(location.search);
    const id = params?.get("id"); // job id

    useEffect(() => {
        const init = async () => {
            if (id) {
                setIsLoading(true);
                const res = await callFetchJobById(id);
                if (res?.data) {
                    setJobDetail(res.data);
                }
                setIsLoading(false);
            }
        };
        init();
    }, [id]);

    const salaryFormatted = jobDetail?.salary 
        ? `${(jobDetail.salary + "").replace(/\B(?=(\d{3})+(?!\d))/g, ',')} đ` 
        : 'Thương lượng';

    return (
        <div className={`${styles["container"]} ${styles["detail-job-section"]}`}>
            <Breadcrumb
                style={{ marginBottom: 20 }}
                items={[
                    {
                        title: <Link to="/"><HomeOutlined /> Trang chủ</Link>,
                    },
                    {
                        title: <Link to="/job">Việc làm IT</Link>,
                    },
                    {
                        title: jobDetail?.name || 'Chi tiết việc làm',
                    },
                ]}
            />

            {isLoading ? (
                <div style={{ background: '#fff', padding: 30, borderRadius: 16 }}>
                    <Skeleton active avatar paragraph={{ rows: 8 }} />
                </div>
            ) : (
                <Row gutter={[24, 24]}>
                    {jobDetail && jobDetail.id && (
                        <>
                            {/* Main Job Content */}
                            <Col span={24} lg={16}>
                                <div className={styles["job-main-card"]}>
                                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: 12 }}>
                                        <div>
                                            <span style={{ 
                                                display: 'inline-block',
                                                padding: '4px 10px',
                                                borderRadius: 6,
                                                background: 'rgba(79, 70, 229, 0.1)',
                                                color: '#4f46e5',
                                                fontWeight: 700,
                                                fontSize: 12,
                                                marginBottom: 8
                                            }}>
                                                VIỆC LÀM IT ĐANG TUYỂN
                                            </span>
                                            <h1 className={styles["job-header-title"]}>
                                                {jobDetail.name}
                                            </h1>
                                            <div style={{ fontSize: 16, color: '#475569', fontWeight: 600, marginBottom: 16 }}>
                                                {jobDetail.company?.name}
                                            </div>
                                        </div>
                                    </div>

                                    {/* Key Attributes Pills */}
                                    <div className={styles["job-pills-row"]}>
                                        <div className={styles["pill-salary"]}>
                                            <DollarCircleFilled /> {salaryFormatted}
                                        </div>
                                        <div className={styles["pill-location"]}>
                                            <EnvironmentOutlined style={{ color: '#0ea5e9' }} /> {getLocationName(jobDetail.location)}
                                        </div>
                                        <div className={styles["pill-time"]}>
                                            <ClockCircleOutlined /> {jobDetail.updatedAt ? dayjs(jobDetail.updatedAt).locale("vi").fromNow() : dayjs(jobDetail.createdAt).locale("vi").fromNow()}
                                        </div>
                                    </div>

                                    {/* Action Button */}
                                    <div style={{ display: 'flex', gap: 12, alignItems: 'center', marginBottom: 20 }}>
                                        <button
                                            onClick={() => setIsModalOpen(true)}
                                            className={styles["btn-apply-cta"]}
                                        >
                                            <RocketOutlined /> Ứng Tuyển Ngay
                                        </button>
                                    </div>

                                    {/* Skills Required */}
                                    {jobDetail?.skills && jobDetail.skills.length > 0 && (
                                        <div style={{ marginBottom: 20 }}>
                                            <div style={{ fontSize: 13, fontWeight: 700, color: '#64748b', textTransform: 'uppercase', marginBottom: 8 }}>
                                                Kỹ Năng Yêu Cầu
                                            </div>
                                            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }}>
                                                {jobDetail.skills.map((item, index) => (
                                                    <Tag 
                                                        key={`${index}-key`} 
                                                        style={{ 
                                                            padding: '4px 12px',
                                                            borderRadius: 6,
                                                            fontSize: 13,
                                                            background: 'rgba(79, 70, 229, 0.08)',
                                                            color: '#4f46e5',
                                                            border: '1px solid rgba(79, 70, 229, 0.2)',
                                                            fontWeight: 600
                                                        }}
                                                    >
                                                        {item.name}
                                                    </Tag>
                                                ))}
                                            </div>
                                        </div>
                                    )}

                                    <Divider />

                                    {/* Detailed Description */}
                                    <div className={styles["job-body-content"]}>
                                        <h3 style={{ fontSize: 18, fontWeight: 700, color: '#0f172a', marginBottom: 16 }}>
                                            Mô Tả Công Việc Chi Tiết
                                        </h3>
                                        {parse(jobDetail.description)}
                                    </div>
                                </div>
                            </Col>

                            {/* Company Sidebar Info */}
                            <Col span={24} lg={8}>
                                <div className={styles["company-sidebar-card"]}>
                                    <div className={styles["company-sidebar-logo"]}>
                                        {jobDetail.company?.logo ? (
                                            <img
                                                alt={jobDetail.company?.name}
                                                src={`${import.meta.env.VITE_BACKEND_URL}/storage/company/${jobDetail.company?.logo}`}
                                                onError={(e: any) => {
                                                    e.target.onerror = null;
                                                    e.target.src = 'https://placehold.co/120x120/4f46e5/ffffff?text=Company';
                                                }}
                                            />
                                        ) : (
                                            <BankOutlined style={{ fontSize: 40, color: '#4f46e5' }} />
                                        )}
                                    </div>
                                    <h3 className={styles["company-sidebar-name"]}>
                                        {jobDetail.company?.name}
                                    </h3>
                                    <div style={{ color: '#10b981', fontSize: 13, fontWeight: 600, display: 'inline-flex', alignItems: 'center', gap: 4, marginBottom: 16 }}>
                                        <CheckCircleFilled /> Doanh nghiệp đã xác thực
                                    </div>

                                    {jobDetail.company?.name && (
                                        <Button 
                                            type="default" 
                                            style={{ width: '100%', borderRadius: 8, height: 40, fontWeight: 600 }}
                                            onClick={() => {
                                                const slug = convertSlug(jobDetail.company?.name ?? "");
                                                navigate(`/company/${slug}?id=${jobDetail.company?.id}`);
                                            }}
                                        >
                                            Xem Trang Công Ty
                                        </Button>
                                    )}

                                    <div style={{ marginTop: 24, textAlign: 'left', padding: '16px', background: '#f8fafc', borderRadius: 12, border: '1px solid #e2e8f0' }}>
                                        <div style={{ fontWeight: 700, color: '#0f172a', fontSize: 14, marginBottom: 8 }}>
                                            💡 Lời khuyên ứng tuyển
                                        </div>
                                        <ul style={{ margin: 0, paddingLeft: 18, color: '#64748b', fontSize: 13, lineHeight: 1.6 }}>
                                            <li>Cập nhật CV mới nhất kèm các dự án nổi bật.</li>
                                            <li>Nêu rõ kinh nghiệm với các công nghệ yêu cầu.</li>
                                            <li>Doanh nghiệp phản hồi nhanh chóng qua email.</li>
                                        </ul>
                                    </div>
                                </div>
                            </Col>
                        </>
                    )}
                </Row>
            )}

            <ApplyModal
                isModalOpen={isModalOpen}
                setIsModalOpen={setIsModalOpen}
                jobDetail={jobDetail}
            />
        </div>
    );
};

export default ClientJobDetailPage;