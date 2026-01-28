import requests
import time
import statistics
import json

# 服务器配置
BASE_URL = "http://localhost:8080/api"

# 测试配置
TEST_ITERATIONS = 100  # 每个接口的测试次数
TEST_USER_ID = 1

# 要测试的接口列表
TEST_ENDPOINTS = [
    {"name": "获取所有分类", "url": f"{BASE_URL}/categories", "method": "GET", "params": {}},  # 获取所有分类
    {"name": "获取单个分类", "url": f"{BASE_URL}/categories/1", "method": "GET", "params": {}},  # 获取单个分类
    {"name": "获取用户信息", "url": f"{BASE_URL}/user/profile", "method": "GET", "params": {}},  # 获取用户信息
    {"name": "获取分类统计", "url": f"{BASE_URL}/statistics/categories", "method": "GET", "params": {"userId": TEST_USER_ID}},  # 获取分类统计
    {"name": "获取趋势统计", "url": f"{BASE_URL}/statistics/trends", "method": "GET", "params": {"userId": TEST_USER_ID}},  # 获取趋势统计
]

# 性能测试结果
performance_results = []

# 执行性能测试
def run_performance_test():
    print("开始性能基准测试...")
    print("=" * 70)
    print(f"测试迭代次数: {TEST_ITERATIONS}")
    print("=" * 70)
    
    for endpoint in TEST_ENDPOINTS:
        print(f"测试接口: {endpoint['name']} ({endpoint['method']} {endpoint['url']})")
        
        response_times = []
        successful_requests = 0
        total_requests = 0
        
        for i in range(TEST_ITERATIONS):
            try:
                # 记录开始时间
                start_time = time.time()
                
                # 发送请求
                if endpoint['method'] == 'GET':
                    response = requests.get(endpoint['url'], params=endpoint['params'], timeout=10)
                elif endpoint['method'] == 'POST':
                    response = requests.post(endpoint['url'], json=endpoint['data'], params=endpoint['params'], timeout=10)
                elif endpoint['method'] == 'PUT':
                    response = requests.put(endpoint['url'], json=endpoint['data'], params=endpoint['params'], timeout=10)
                elif endpoint['method'] == 'DELETE':
                    response = requests.delete(endpoint['url'], params=endpoint['params'], timeout=10)
                else:
                    print(f"不支持的请求方法: {endpoint['method']}")
                    continue
                
                # 记录响应时间
                end_time = time.time()
                response_time = (end_time - start_time) * 1000  # 转换为毫秒
                
                # 检查响应状态
                if response.status_code == 200:
                    successful_requests += 1
                    response_times.append(response_time)
                    
                total_requests += 1
                
                # 每10次请求输出一次进度
                if (i + 1) % 10 == 0:
                    print(f"  进度: {i + 1}/{TEST_ITERATIONS}")
                    
            except Exception as e:
                print(f"  请求失败: {str(e)}")
                total_requests += 1
                continue
        
        # 计算性能指标
        if response_times:
            # 基本统计信息
            min_response_time = min(response_times)
            max_response_time = max(response_times)
            avg_response_time = statistics.mean(response_times)
            median_response_time = statistics.median(response_times)
            
            # 百分位统计
            sorted_times = sorted(response_times)
            p50 = sorted_times[int(len(sorted_times) * 0.5)]
            p90 = sorted_times[int(len(sorted_times) * 0.9)]
            p95 = sorted_times[int(len(sorted_times) * 0.95)]
            p99 = sorted_times[int(len(sorted_times) * 0.99)]
            
            # 吞吐量 (请求/秒)
            total_time = sum(response_times) / 1000  # 转换为秒
            throughput = len(response_times) / total_time if total_time > 0 else 0
            
            # 成功率
            success_rate = (successful_requests / total_requests) * 100
            
            # 记录测试结果
            result = {
                "endpoint": endpoint["name"],
                "url": endpoint["url"],
                "method": endpoint["method"],
                "iterations": TEST_ITERATIONS,
                "successful_requests": successful_requests,
                "total_requests": total_requests,
                "success_rate": success_rate,
                "min_response_time": min_response_time,
                "max_response_time": max_response_time,
                "avg_response_time": avg_response_time,
                "median_response_time": median_response_time,
                "p50_response_time": p50,
                "p90_response_time": p90,
                "p95_response_time": p95,
                "p99_response_time": p99,
                "throughput": throughput,
                "response_times": response_times
            }
            
            performance_results.append(result)
            
            # 输出测试结果
            print("=" * 70)
            print(f"接口: {endpoint['name']}")
            print(f"URL: {endpoint['url']}")
            print(f"请求方法: {endpoint['method']}")
            print(f"测试次数: {TEST_ITERATIONS}")
            print(f"成功次数: {successful_requests}")
            print(f"失败次数: {total_requests - successful_requests}")
            print(f"成功率: {success_rate:.2f}%")
            print(f"最小响应时间: {min_response_time:.2f} ms")
            print(f"最大响应时间: {max_response_time:.2f} ms")
            print(f"平均响应时间: {avg_response_time:.2f} ms")
            print(f"中位数响应时间: {median_response_time:.2f} ms")
            print(f"50% 响应时间: {p50:.2f} ms")
            print(f"90% 响应时间: {p90:.2f} ms")
            print(f"95% 响应时间: {p95:.2f} ms")
            print(f"99% 响应时间: {p99:.2f} ms")
            print(f"吞吐量: {throughput:.2f} 请求/秒")
            print("=" * 70)
        else:
            print("=" * 70)
            print(f"接口: {endpoint['name']}")
            print(f"所有请求都失败了！")
            print("=" * 70)

# 生成性能测试报告
def generate_performance_report():
    print("\n生成性能测试报告...")
    
    # 保存详细测试结果到JSON文件
    with open("performance_test_results.json", "w", encoding="utf-8") as f:
        json.dump(performance_results, f, ensure_ascii=False, indent=2)
    
    # 生成Markdown格式的报告
    report_content = "# API性能测试报告\n\n"
    report_content += f"## 测试概述\n\n"
    report_content += f"- 测试时间: {time.strftime('%Y-%m-%d %H:%M:%S')}\n"
    report_content += f"- 测试迭代次数: {TEST_ITERATIONS}\n"
    report_content += f"- 测试用户ID: {TEST_USER_ID}\n\n"
    
    # 性能汇总表格
    report_content += "## 性能汇总\n\n"
    report_content += "| 接口名称 | 成功率 | 平均响应时间 (ms) | 90% 响应时间 (ms) | 99% 响应时间 (ms) | 吞吐量 (请求/秒) |\n"
    report_content += "|---------|-------|------------------|------------------|------------------|----------------|\n"
    
    for result in performance_results:
        report_content += f"| {result['endpoint']} | {result['success_rate']:.2f}% | {result['avg_response_time']:.2f} | {result['p90_response_time']:.2f} | {result['p99_response_time']:.2f} | {result['throughput']:.2f} |\n"
    
    # 详细测试结果
    report_content += "\n## 详细测试结果\n\n"
    
    for result in performance_results:
        report_content += f"### {result['endpoint']}\n\n"
        report_content += f"**URL**: {result['url']}\n"
        report_content += f"**请求方法**: {result['method']}\n"
        report_content += f"**测试次数**: {result['iterations']}\n"
        report_content += f"**成功次数**: {result['successful_requests']}\n"
        report_content += f"**失败次数**: {result['total_requests'] - result['successful_requests']}\n"
        report_content += f"**成功率**: {result['success_rate']:.2f}%\n\n"
        
        report_content += "#### 响应时间统计\n\n"
        report_content += f"- 最小响应时间: {result['min_response_time']:.2f} ms\n"
        report_content += f"- 最大响应时间: {result['max_response_time']:.2f} ms\n"
        report_content += f"- 平均响应时间: {result['avg_response_time']:.2f} ms\n"
        report_content += f"- 中位数响应时间: {result['median_response_time']:.2f} ms\n\n"
        
        report_content += "#### 百分位响应时间\n\n"
        report_content += f"- 50% 响应时间: {result['p50_response_time']:.2f} ms\n"
        report_content += f"- 90% 响应时间: {result['p90_response_time']:.2f} ms\n"
        report_content += f"- 95% 响应时间: {result['p95_response_time']:.2f} ms\n"
        report_content += f"- 99% 响应时间: {result['p99_response_time']:.2f} ms\n\n"
        
        report_content += "#### 吞吐量\n\n"
        report_content += f"- 吞吐量: {result['throughput']:.2f} 请求/秒\n\n"
    
    # 保存报告到Markdown文件
    with open("performance_test_report.md", "w", encoding="utf-8") as f:
        f.write(report_content)
    
    print("性能测试报告已生成：performance_test_report.md")
    print("详细测试结果已保存：performance_test_results.json")

# 运行性能测试
if __name__ == "__main__":
    run_performance_test()
    generate_performance_report()
    print("\n性能基准测试完成！")
