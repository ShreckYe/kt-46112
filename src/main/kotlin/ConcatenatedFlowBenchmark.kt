package benchmark

import kotlinx.benchmark.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.openjdk.jmh.annotations.Fork
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class)
operator fun <T> Flow<T>.plus(other: Flow<T>) =
    flowOf(this, other).flattenConcat()

fun <T> Flow<T>.blockingCollect(collector: FlowCollector<T>) =
    runBlocking {
        collect(collector)
    }


// see: https://github.com/Kotlin/kotlinx-benchmark/blob/master/examples/kotlin-multiplatform/src/jvmMain/kotlin/JvmTestBenchmark.kt
@State(Scope.Benchmark)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
class ConcatenatedFlowBenchmark {
    private lateinit var prependedFlow10: Flow<Unit>
    private lateinit var prependedFlow100: Flow<Unit>
    private lateinit var prependedFlow1000: Flow<Unit>
    private lateinit var appendedFlow10: Flow<Unit>
    private lateinit var appendedFlow100: Flow<Unit>
    private lateinit var appendedFlow1000: Flow<Unit>

    fun prependedFlow(length: Int): Flow<Unit> =
        if (length == 0) emptyFlow()
        else flowOf(Unit) + prependedFlow(length - 1)

    fun appendedFlow(length: Int): Flow<Unit> =
        if (length == 0) emptyFlow()
        else prependedFlow(length - 1) + flowOf(Unit)

    @Setup
    fun setup() {
        prependedFlow10 = prependedFlow(10)
        prependedFlow100 = prependedFlow(100)
        prependedFlow1000 = prependedFlow(1000)
        appendedFlow10 = appendedFlow(10)
        appendedFlow100 = appendedFlow(100)
        appendedFlow1000 = appendedFlow(1000)
    }

    @Benchmark
    fun prependedFlow10Benchmark() = prependedFlow10.blockingCollect { }

    @Benchmark
    fun prependedFlow100Benchmark() = prependedFlow100.blockingCollect { }

    @Benchmark
    fun prependedFlow1000Benchmark() = prependedFlow1000.blockingCollect { }

    @Benchmark
    fun appendedFlow10Benchmark() = appendedFlow10.blockingCollect { }

    @Benchmark
    fun appendedFlow100Benchmark() = appendedFlow100.blockingCollect { }

    @Benchmark
    fun appendedFlow1000Benchmark() = appendedFlow1000.blockingCollect { }
}