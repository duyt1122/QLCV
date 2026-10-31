import { useAppSelector } from "@/redux/hooks";
import { IJob, IResumeAiMatch } from "@/types/backend";
import { ProForm, ProFormText } from "@ant-design/pro-components";
import { Button, Col, ConfigProvider, Divider, Modal, Row, Upload, message, notification } from "antd";
import { useNavigate } from "react-router-dom";
import enUS from 'antd/lib/locale/en_US';
import { UploadOutlined } from '@ant-design/icons';
import type { UploadProps } from 'antd';
import { callAnalyzeResumeMatch, callCreateResume, callUploadSingleFile } from "@/config/api";
import { useState } from 'react';

interface IProps {
    isModalOpen: boolean;
    setIsModalOpen: (v: boolean) => void;
    jobDetail: IJob | null;
}

const ApplyModal = (props: IProps) => {
    const { isModalOpen, setIsModalOpen, jobDetail } = props;
    const isAuthenticated = useAppSelector(state => state.account.isAuthenticated);
    const user = useAppSelector(state => state.account.user);
    const [urlCV, setUrlCV] = useState<string>("");
    const [aiAnalysis, setAiAnalysis] = useState<IResumeAiMatch | null>(null);
    const [isAnalyzing, setIsAnalyzing] = useState<boolean>(false);

    const navigate = useNavigate();

    const handleAnalyzeCv = async (fileName: string) => {
        if (!jobDetail?.id || !fileName) return;
        setIsAnalyzing(true);
        try {
            const res = await callAnalyzeResumeMatch(fileName, jobDetail.id);
            if (res.data) {
                setAiAnalysis(res.data);
            }
        } catch (error: any) {
            setAiAnalysis(null);
            message.warning(error?.response?.data?.message ?? "Không thể đánh giá CV bằng AI lúc này.");
        } finally {
            setIsAnalyzing(false);
        }
    }

    const handleOkButton = async () => {
        if (!urlCV && isAuthenticated) {
            message.error("Vui lòng upload CV!");
            return;
        }

        if (!isAuthenticated) {
            setIsModalOpen(false);
            navigate(`/login?callback=${window.location.href}`)
        }
        else {
            //todo
            if (jobDetail) {
                const res = await callCreateResume(urlCV, jobDetail?.id, user.email, user.id);
                if (res.data) {
                    message.success("Nộp hồ sơ ứng tuyển thành công!");
                    setIsModalOpen(false);
                    setAiAnalysis(null);
                    setUrlCV("");
                } else {
                    notification.error({
                        message: 'Có lỗi xảy ra',
                        description: res.message
                    });
                }
            }
        }
    }

    const propsUpload: UploadProps = {
        maxCount: 1,
        multiple: false,
        accept: "application/pdf,application/msword, .doc, .docx, .pdf",
        async customRequest({ file, onSuccess, onError }: any) {
            const res = await callUploadSingleFile(file, "resume");
            if (res && res.data) {
                const fileName = res.data.fileName;
                setUrlCV(fileName);
                if (jobDetail?.id) {
                    await handleAnalyzeCv(fileName);
                }
                if (onSuccess) onSuccess('ok')
            } else {
                if (onError) {
                    setUrlCV("");
                    const error = new Error(res.message);
                    onError({ event: error });
                }
            }
        },
        onChange(info) {
            if (info.file.status !== 'uploading') {
                // console.log(info.file, info.fileList);
            }
            if (info.file.status === 'done') {
                message.success(`${info.file.name} tải lên thành công`);
            } else if (info.file.status === 'error') {
                message.error(info?.file?.error?.event?.message ?? "Đã có lỗi xảy ra khi tải tệp lên.")
            }
        },
    };


    return (
        <>
            <Modal title="Ứng Tuyển Vị Trí Công Nghệ"
                open={isModalOpen}
                onOk={() => handleOkButton()}
                onCancel={() => {
                    setIsModalOpen(false);
                    setAiAnalysis(null);
                    setUrlCV("");
                }}
                maskClosable={false}
                okText={isAuthenticated ? "Nộp Hồ Sơ Ngay" : "Đăng Nhập Để Ứng Tuyển"}
                cancelButtonProps={
                    { style: { display: "none" } }
                }
                destroyOnClose={true}
            >
                <Divider />
                {isAuthenticated ?
                    <div>
                        <ConfigProvider locale={enUS}>
                            <ProForm
                                submitter={{
                                    render: () => <></>
                                }}
                            >
                                <Row gutter={[10, 10]}>
                                    <Col span={24}>
                                        <div style={{ fontSize: 15, marginBottom: 8 }}>
                                            Bạn đang ứng tuyển vị trí: <strong style={{ color: '#4f46e5' }}>{jobDetail?.name}</strong> tại <strong>{jobDetail?.company?.name}</strong>
                                        </div>
                                    </Col>
                                    <Col span={24}>
                                        <ProFormText
                                            fieldProps={{
                                                type: "email"
                                            }}
                                            label="Email Ứng Viên"
                                            name={"email"}
                                            labelAlign="right"
                                            disabled
                                            initialValue={user?.email}
                                        />
                                    </Col>
                                    <Col span={24}>
                                        <ProForm.Item
                                            label={"Tệp Hồ Sơ (CV)"}
                                            rules={[{ required: true, message: 'Vui lòng tải lên file CV của bạn!' }]}
                                        >

                                            <Upload {...propsUpload}>
                                                <Button icon={<UploadOutlined />}>Tải lên CV (Hỗ trợ *.pdf, *.doc, *.docx &lt; 5MB)</Button>
                                            </Upload>
                                        </ProForm.Item>
                                    </Col>
                                    {isAnalyzing && (
                                        <Col span={24}>
                                            <div style={{ padding: '10px 12px', background: '#f5f3ff', borderRadius: 8, color: '#4f46e5', fontWeight: 600 }}>
                                                AI đang chấm điểm mức độ phù hợp của CV...
                                            </div>
                                        </Col>
                                    )}
                                    {aiAnalysis && (
                                        <Col span={24}>
                                            <div style={{ background: '#f8fafc', border: '1px solid #e2e8f0', borderRadius: 10, padding: 12 }}>
                                                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 8 }}>
                                                    <strong>AI đánh giá CV</strong>
                                                    <span style={{ background: aiAnalysis.matchScore >= 80 ? '#dcfce7' : aiAnalysis.matchScore >= 60 ? '#fef3c7' : '#fee2e2', color: aiAnalysis.matchScore >= 80 ? '#166534' : aiAnalysis.matchScore >= 60 ? '#92400e' : '#991b1b', padding: '4px 10px', borderRadius: 999, fontWeight: 700 }}>
                                                        {aiAnalysis.matchScore}%
                                                    </span>
                                                </div>
                                                <div style={{ marginBottom: 8, color: '#334155' }}>{aiAnalysis.summary}</div>
                                                <div style={{ marginBottom: 6 }}><strong>Kỹ năng phù hợp:</strong> {aiAnalysis.matchedSkills.length ? aiAnalysis.matchedSkills.join(', ') : 'Chưa rõ'}</div>
                                                <div style={{ marginBottom: 6 }}><strong>Cần bổ sung:</strong> {aiAnalysis.missingSkills.length ? aiAnalysis.missingSkills.join(', ') : 'Không có'}</div>
                                                <div><strong>Gợi ý cải thiện:</strong>
                                                    <ul style={{ margin: '8px 0 0 20px', padding: 0 }}>
                                                        {aiAnalysis.suggestions.map((item, idx) => (
                                                            <li key={idx}>{item}</li>
                                                        ))}
                                                    </ul>
                                                </div>
                                            </div>
                                        </Col>
                                    )}
                                </Row>

                            </ProForm>
                        </ConfigProvider>
                    </div>
                    :
                    <div style={{ padding: '16px 0', fontSize: 14, color: '#475569' }}>
                        Bạn chưa đăng nhập hệ thống DevHire. Vui lòng đăng nhập để hoàn tất nộp hồ sơ ứng tuyển trực tiếp đến nhà tuyển dụng.
                    </div>
                }
                <Divider />
            </Modal>
        </>
    )
}
export default ApplyModal;
