import { Button, Col, Form, Row, Select, notification, Tag } from 'antd';
import { EnvironmentOutlined, MonitorOutlined, SearchOutlined, FireOutlined } from '@ant-design/icons';
import { LOCATION_LIST } from '@/config/utils';
import { ProForm } from '@ant-design/pro-components';
import { useEffect, useState } from 'react';
import { callFetchAllSkill } from '@/config/api';
import { useLocation, useNavigate, useSearchParams } from 'react-router-dom';

const SearchClient = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const optionsLocations = LOCATION_LIST;
    const [form] = Form.useForm();
    const [optionsSkills, setOptionsSkills] = useState<{
        label: string;
        value: string;
    }[]>([]);

    const [searchParams, setSearchParams] = useSearchParams();

    useEffect(() => {
        if (location.search) {
            const queryLocation = searchParams.get("location");
            const querySkills = searchParams.get("skills");
            if (queryLocation) {
                form.setFieldValue("location", queryLocation.split(","));
            }
            if (querySkills) {
                form.setFieldValue("skills", querySkills.split(","));
            }
        }
    }, [location.search]);

    useEffect(() => {
        fetchSkill();
    }, []);

    const fetchSkill = async () => {
        let query = `page=1&size=100&sort=createdAt,desc`;

        const res = await callFetchAllSkill(query);
        if (res && res.data) {
            const arr = res?.data?.result?.map(item => {
                return {
                    label: item.name as string,
                    value: item.id + "" as string
                }
            }) ?? [];
            setOptionsSkills(arr);
        }
    };

    const onFinish = async (values: any) => {
        let query = "";
        if (values?.location?.length) {
            query = `location=${values?.location?.join(",")}`;
        }
        if (values?.skills?.length) {
            query = values.location?.length ? query + `&skills=${values?.skills?.join(",")}`
                : `skills=${values?.skills?.join(",")}`;
        }

        if (!query) {
            notification.error({
                message: 'Thông báo',
                description: "Vui lòng chọn kỹ năng hoặc địa điểm để tìm kiếm!"
            });
            return;
        }
        navigate(`/job?${query}`);
    };

    const handleQuickTagClick = (skillId: string) => {
        form.setFieldValue("skills", [skillId]);
        navigate(`/job?skills=${skillId}`);
    };

    return (
        <div style={{ width: '100%' }}>
            <ProForm
                form={form}
                onFinish={onFinish}
                submitter={{
                    render: () => <></>
                }}
            >
                <Row gutter={[16, 16]} align="middle">
                    <Col span={24} md={14}>
                        <ProForm.Item name="skills" style={{ marginBottom: 0 }}>
                            <Select
                                mode="multiple"
                                allowClear
                                size="large"
                                suffixIcon={null}
                                style={{ width: '100%' }}
                                placeholder={
                                    <span style={{ color: '#94a3b8' }}>
                                        <MonitorOutlined style={{ color: '#4f46e5', marginRight: 8 }} />
                                        Tìm kiếm theo kỹ năng (React, Java, NodeJS, Cloud...)...
                                    </span>
                                }
                                optionLabelProp="label"
                                options={optionsSkills}
                            />
                        </ProForm.Item>
                    </Col>
                    <Col span={16} md={6}>
                        <ProForm.Item name="location" style={{ marginBottom: 0 }}>
                            <Select
                                mode="multiple"
                                allowClear
                                size="large"
                                suffixIcon={null}
                                style={{ width: '100%' }}
                                placeholder={
                                    <span style={{ color: '#94a3b8' }}>
                                        <EnvironmentOutlined style={{ color: '#10b981', marginRight: 8 }} />
                                        Địa điểm làm việc...
                                    </span>
                                }
                                optionLabelProp="label"
                                options={optionsLocations}
                            />
                        </ProForm.Item>
                    </Col>
                    <Col span={8} md={4}>
                        <Button 
                            type='primary' 
                            size="large"
                            icon={<SearchOutlined />}
                            onClick={() => form.submit()}
                            style={{ 
                                width: '100%', 
                                height: 42,
                                fontWeight: 700,
                                background: 'linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%)',
                                border: 'none',
                                boxShadow: '0 4px 14px rgba(79, 70, 229, 0.4)'
                            }}
                        >
                            Tìm Việc
                        </Button>
                    </Col>
                </Row>
            </ProForm>

            {/* Quick Filter Tag Pills */}
            <div style={{ marginTop: 14, display: 'flex', alignItems: 'center', flexWrap: 'wrap', gap: 6 }}>
                <span style={{ fontSize: 13, color: '#64748b', display: 'flex', alignItems: 'center', gap: 4 }}>
                    <FireOutlined style={{ color: '#f59e0b' }} /> Từ khóa gợi ý:
                </span>
                {optionsSkills.slice(0, 7).map((skill) => (
                    <Tag 
                        key={skill.value} 
                        style={{ 
                            cursor: 'pointer', 
                            borderRadius: 6, 
                            border: '1px solid #e2e8f0',
                            backgroundColor: '#f8fafc',
                            color: '#475569',
                            padding: '2px 8px',
                            transition: 'all 0.2s ease'
                        }}
                        onClick={() => handleQuickTagClick(skill.value)}
                    >
                        {skill.label}
                    </Tag>
                ))}
            </div>
        </div>
    );
};

export default SearchClient;