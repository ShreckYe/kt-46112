package benchmark

import kotlinx.benchmark.*
import org.openjdk.jmh.annotations.Fork
import java.util.concurrent.TimeUnit

// see: https://github.com/Kotlin/kotlinx-benchmark/blob/master/examples/kotlin-multiplatform/src/jvmMain/kotlin/JvmTestBenchmark.kt
@State(Scope.Benchmark)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
class ConcatenatedSequenceBenchmark {
    private lateinit var prependedSequence10: Sequence<Unit>
    private lateinit var prependedSequence100: Sequence<Unit>
    private lateinit var prependedSequence1000: Sequence<Unit>
    private lateinit var appendedSequence10: Sequence<Unit>
    private lateinit var appendedSequence100: Sequence<Unit>
    private lateinit var appendedSequence1000: Sequence<Unit>

    fun prependedSequence(length: Int): Sequence<Unit> =
        if (length == 0) emptySequence()
        else sequenceOf(Unit) + prependedSequence(length - 1)

    fun appendedSequence(length: Int): Sequence<Unit> =
        if (length == 0) emptySequence()
        else prependedSequence(length - 1) + sequenceOf(Unit)

    @Setup
    fun setup() {
        prependedSequence10 = prependedSequence(10)
        prependedSequence100 = prependedSequence(100)
        prependedSequence1000 = prependedSequence(1000)
        appendedSequence10 = appendedSequence(10)
        appendedSequence100 = appendedSequence(100)
        appendedSequence1000 = appendedSequence(1000)
    }

    @Benchmark
    fun prependedSequence10Benchmark() = prependedSequence10.forEach { }

    @Benchmark
    fun prependedSequence100Benchmark() = prependedSequence100.forEach { }

    @Benchmark

    fun prependedSequence1000Benchmark() = prependedSequence1000.forEach { }

    @Benchmark
    fun appendedSequence10Benchmark() = appendedSequence10.forEach { }

    @Benchmark
    fun appendedSequence100Benchmark() = appendedSequence100.forEach { }

    @Benchmark
    fun appendedSequence1000Benchmark() = appendedSequence1000.forEach { }
}