import { callFetchJob } from '@/config/api';
import { convertSlug, getLocationName } from '@/config/utils';
import { IJob } from '@/types/backend';
import { 
    EnvironmentOutlined, 
    ThunderboltFilled, 
    ArrowRightOutlined, 
    ClockCircleOutlined,
    BankOutlined,
    DollarCircleFilled
} from '@ant-design/icons';
import { Col, Empty, Pagination, Row, Spin, Tag } from 'antd';
import { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate, useSearchParams } from 'react-router-dom';
import styles from 'styles/client.module.scss';
import { sfIn } from "spring-filter-query-builder";

import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
dayjs.extend(relativeTime);

interface IProps {
    showPagination?: boolean;
}

const JobCard = (props: IProps) => {
    const { showPagination = false } = props;

    const [displayJob, setDisplayJob] = useState<IJob[] | null>(null);
    const [isLoading, setIsLoading] = useState<boolean>(false);

    const [current, setCurrent] = useState(1);
    const [pageSize, setPageSize] = useState(6);
    const [total, setTotal] = useState(0);
    const [filter, setFilter] = useState("");
    const [sortQuery, setSortQuery] = useState("sort=updatedAt,desc");
    const navigate = useNavigate();
    const [searchParams, setSearchParams] = useSearchParams();
    const location = useLocation();

    useEffect(() => {
        fetchJob();
    }, [current, pageSize, filter, sortQuery, location]);

    const fetchJob = async () => {
        setIsLoading(true);
        let query = `page=${current}&size=${pageSize}`;
        if (filter) {
            query += `&${filter}`;
        }
        if (sortQuery) {
            query += `&${sortQuery}`;
        }

        //check query string
        const queryLocation = searchParams.get("location");
        const querySkills = searchParams.get("skills");
        if (queryLocation || querySkills) {
            let q = "";
            if (queryLocation) {
                q = sfIn("location", queryLocation.split(",")).toString();
            }

            if (querySkills) {
                q = queryLocation ?
                    q + " and " + `${sfIn("skills", querySkills.split(","))}`
                    : `${sfIn("skills", querySkills.split(","))}`;
            }

            query += `&filter=${encodeURIComponent(q)}`;
        }

        const res = await callFetchJob(query);
        if (res && res.data) {
            setDisplayJob(res.data.result);
            setTotal(res.data.meta.total);
        }
        setIsLoading(false);
    };

    const handleOnchangePage = (pagination: { current: number, pageSize: number }) => {
        if (pagination && pagination.current !== current) {
            setCurrent(pagination.current);
        }
        if (pagination && pagination.pageSize !== pageSize) {
            setPageSize(pagination.pageSize);
            setCurrent(1);
        }
    };

    const handleViewDetailJob = (item: IJob) => {
        const slug = convertSlug(item.name);
        navigate(`/job/${slug}?id=${item.id}`);
    };

    return (
        <div className={styles["card-job-section"]}>
            {/* Header */}
            <div className={styles["section-header"]}>
                <div className={styles["section-title-wrap"]}>
                    <span className={styles["section-tag"]}>VỊ TRÍ MỚI NHẤT</span>
                    <h2 className={styles["section-title"]}>Cơ Hội Việc Làm Nổi Bật</h2>
                </div>
                {!showPagination && (
                    <Link to="/job" className={styles["view-all-link"]}>
                        Xem tất cả {total > 0 ? `(${total})` : ''} <ArrowRightOutlined />
                    </Link>
                )}
            </div>

            <Spin spinning={isLoading} tip="Đang tải danh sách việc làm...">
                <Row gutter={[20, 20]}>
                    {displayJob?.map(item => {
                        const salaryText = item.salary 
                            ? `${(item.salary + "").replace(/\B(?=(\d{3})+(?!\d))/g, ',')} đ` 
                            : 'Thương lượng';

                        return (
                            <Col span={24} md={12} key={item.id}>
                                <div 
                                    className={styles["job-card"]}
                                    onClick={() => handleViewDetailJob(item)}
                                >
                                    <div className={styles["card-inner"]}>
                                        <div className={styles["company-logo-box"]}>
                                            {item?.company?.logo ? (
                                                <img
                                                    alt={item.name}
                                                    src={`${import.meta.env.VITE_BACKEND_URL}/storage/company/${item?.company?.logo}`}
                                                    onError={(e: any) => {
                                                        e.target.onerror = null;
                                                        e.target.src = 'https://placehold.co/100x100/4f46e5/ffffff?text=Tech';
                                                    }}
                                                />
                                            ) : (
                                                <BankOutlined style={{ fontSize: 26, color: '#6366f1' }} />
                                            )}
                                        </div>

                                        <div className={styles["job-info"]}>
                                            <div className={styles["job-title"]} title={item.name}>
                                                {item.name}
                                            </div>
                                            <div className={styles["company-name"]}>
                                                {item?.company?.name || 'Doanh nghiệp Công nghệ'}
                                            </div>

                                            <div className={styles["meta-tags"]}>
                                                <span className={styles["salary-chip"]}>
                                                    <DollarCircleFilled /> {salaryText}
                                                </span>
                                                <span className={styles["location-chip"]}>
                                                    <EnvironmentOutlined style={{ color: '#0ea5e9' }} /> {getLocationName(item.location)}
                                                </span>
                                            </div>

                                            <div className={styles["card-bottom"]}>
                                                <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
                                                    <ClockCircleOutlined /> 
                                                    {item.updatedAt ? dayjs(item.updatedAt).locale('vi').fromNow() : dayjs(item.createdAt).locale('vi').fromNow()}
                                                </span>
                                                <span style={{ color: '#4f46e5', fontWeight: 600, display: 'flex', alignItems: 'center', gap: 4 }}>
                                                    Ứng tuyển <ArrowRightOutlined style={{ fontSize: 11 }} />
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </Col>
                        );
                    })}

                    {(!displayJob || displayJob.length === 0) && !isLoading && (
                        <Col span={24}>
                            <div style={{ padding: '60px 0', textAlign: 'center', background: '#fff', borderRadius: 16 }}>
                                <Empty description="Hiện chưa có việc làm nào phù hợp với bộ lọc tìm kiếm." />
                            </div>
                        </Col>
                    )}
                </Row>

                {showPagination && total > 0 && (
                    <div style={{ marginTop: 36, display: 'flex', justifyContent: 'center' }}>
                        <Pagination
                            current={current}
                            total={total}
                            pageSize={pageSize}
                            responsive
                            showSizeChanger={false}
                            onChange={(p: number, s: number) => handleOnchangePage({ current: p, pageSize: s })}
                        />
                    </div>
                )}
            </Spin>
        </div>
    );
};

export default JobCard;