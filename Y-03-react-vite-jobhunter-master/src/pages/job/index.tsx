import SearchClient from '@/components/client/search.client';
import styles from 'styles/client.module.scss';
import JobCard from '@/components/client/card/job.card';
import { Breadcrumb } from 'antd';
import { HomeOutlined } from '@ant-design/icons';
import { Link } from 'react-router-dom';

const ClientJobPage = (props: any) => {
    return (
        <div className={styles["container"]} style={{ paddingTop: 24, paddingBottom: 60 }}>
            <Breadcrumb
                style={{ marginBottom: 20 }}
                items={[
                    {
                        title: <Link to="/"><HomeOutlined /> Trang chủ</Link>,
                    },
                    {
                        title: 'Việc làm IT',
                    },
                ]}
            />

            <div style={{ 
                background: '#ffffff', 
                padding: '24px 28px', 
                borderRadius: 16, 
                border: '1px solid #e2e8f0',
                boxShadow: '0 4px 16px rgba(0,0,0,0.02)',
                marginBottom: 32
            }}>
                <SearchClient />
            </div>

            <JobCard showPagination={true} />
        </div>
    );
};

export default ClientJobPage;