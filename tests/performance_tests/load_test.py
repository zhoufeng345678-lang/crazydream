import requests
import threading
import time
import statistics
import sys

BASE_URL = 'http://localhost:8080/api'
USER_ID = 1

# 测试配置
NUM_THREADS = 50          # 并发用户数
TEST_DURATION = 30        # 测试持续时间（秒）
SLEEP_TIME = 0.1          # 每个线程的请求间隔（秒）

# 测试结果统计
results = {
    '/categories': {'times': [], 'success': 0, 'failure': 0},
    '/statistics/categories': {'times': [], 'success': 0, 'failure': 0},
    '/statistics/trends': {'times': [], 'success': 0, 'failure': 0}
}

# 线程锁
lock = threading.Lock()

# 测试函数
def test_endpoint(endpoint, params=None):
    url = f"{BASE_URL}{endpoint}"
    if params:
        # 添加用户ID参数
        params['userId'] = USER_ID
    else:
        params = {'userId': USER_ID}
    
    try:
        start_time = time.time()
        response = requests.get(url, params=params)
        end_time = time.time()
        
        response_time = (end_time - start_time) * 1000  # 转换为毫秒
        
        with lock:
            if response.status_code == 200:
                results[endpoint]['success'] += 1
                results[endpoint]['times'].append(response_time)
            else:
                results[endpoint]['failure'] += 1
                
    except Exception as e:
        with lock:
            results[endpoint]['failure'] += 1

# 工作线程函数
def worker():
    start_time = time.time()
    endpoints = list(results.keys())
    endpoint_index = 0
    
    while time.time() - start_time < TEST_DURATION:
        endpoint = endpoints[endpoint_index % len(endpoints)]
        test_endpoint(endpoint)
        endpoint_index += 1
        time.sleep(SLEEP_TIME)

# 启动负载测试
def run_load_test():
    print(f"\n开始负载测试...")
    print(f"并发用户数: {NUM_THREADS}")
    print(f"测试持续时间: {TEST_DURATION} 秒")
    print(f"请求间隔: {SLEEP_TIME} 秒")
    print("=" * 60)
    
    # 创建并启动线程
    threads = []
    for i in range(NUM_THREADS):
        thread = threading.Thread(target=worker)
        threads.append(thread)
        thread.start()
    
    # 等待所有线程完成
    for thread in threads:
        thread.join()
    
    # 计算吞吐量
    total_requests = sum(result['success'] + result['failure'] for result in results.values())
    throughput = total_requests / TEST_DURATION
    
    # 输出测试结果
    print(f"\n负载测试完成！")
    print("=" * 60)
    print(f"总请求数: {total_requests}")
    print(f"吞吐量: {throughput:.2f} 请求/秒")
    print(f"测试持续时间: {TEST_DURATION} 秒")
    print("\n各接口测试结果:")
    print("=" * 60)
    
    for endpoint, result in results.items():
        total = result['success'] + result['failure']
        if total == 0:
            continue
            
        success_rate = (result['success'] / total) * 100
        times = result['times']
        
        print(f"\n接口: {endpoint}")
        print(f"  请求总数: {total}")
        print(f"  成功数: {result['success']}")
        print(f"  失败数: {result['failure']}")
        print(f"  成功率: {success_rate:.2f}%")
        
        if times:
            min_time = min(times)
            max_time = max(times)
            avg_time = statistics.mean(times)
            median_time = statistics.median(times)
            
            # 计算百分位数
            sorted_times = sorted(times)
            p90 = sorted_times[int(len(sorted_times) * 0.9)]
            p95 = sorted_times[int(len(sorted_times) * 0.95)]
            p99 = sorted_times[int(len(sorted_times) * 0.99)] if len(sorted_times) >= 100 else sorted_times[-1]
            
            print(f"  最小响应时间: {min_time:.2f} ms")
            print(f"  最大响应时间: {max_time:.2f} ms")
            print(f"  平均响应时间: {avg_time:.2f} ms")
            print(f"  中位数响应时间: {median_time:.2f} ms")
            print(f"  90% 响应时间: {p90:.2f} ms")
            print(f"  95% 响应时间: {p95:.2f} ms")
            print(f"  99% 响应时间: {p99:.2f} ms")

# 执行负载测试
if __name__ == "__main__":
    run_load_test()