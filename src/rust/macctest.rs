#![allow(dead_code, mutable_transmutes, non_camel_case_types, non_snake_case, non_upper_case_globals, unused_assignments, unused_mut)]
unsafe extern "C" {
    fn malloc(_: libc::c_ulong) -> *mut libc::c_void;
    fn free(_: *mut libc::c_void);
    fn clock() -> u32;
}

#[unsafe(no_mangle)]
pub static mut seed: u64 = 50429807834;
static multiplier: u64 = 25214903917;
static addend: u64 = 11;
static mask: u64 = (1 << 48) - 1;

#[unsafe(no_mangle)]
pub unsafe extern "C" fn macc_rand(bits: u8) -> u64 {
    let mut nextSeed: u64 = (seed * multiplier + addend) & mask;
    seed = nextSeed;
    return nextSeed >> (48 - bits);
}

#[unsafe(no_mangle)]
pub unsafe extern "C" fn testMem(memPtr: *mut u8, memBits: u8) {
    let memSize: isize = 1 << memBits;

    seed = 50429807834;

    // sequential write
    let mut startTime: u32 = clock();
    let mut i: isize = 0; while i < memSize {
        *memPtr.offset(i) = macc_rand(8) as u8;
        i += 1;
    }
    let mut elapsedTime: u32 = clock() - startTime;
    println!("Sequential write: {elapsedTime}");

    // random read
    let mut result: u64 = 0;
    startTime = clock();
    i = 0; while i < memSize {
        result += *memPtr.offset(macc_rand(memBits) as isize) as u64;
        i += 1;
    }
    elapsedTime = clock() - startTime;
    println!("Random read: {elapsedTime}");

    // random write
    startTime = clock();
    i = 0; while i < memSize {
        let mut r: u8  = macc_rand(8) as u8;
        *memPtr.offset(macc_rand(memBits) as isize) = r;
        i += 1;
    }
    elapsedTime = clock() - startTime;
    println!("Random write: {elapsedTime}");

    // sequential read
    startTime = clock();
    i = 0; while i < memSize {
        result += *memPtr.offset(i) as u64;
        i += 1;
    }
    elapsedTime = clock() - startTime;
    println!("Sequential read: {elapsedTime}");

    println!("Result: {result}");
    println!();
}

unsafe fn main_0() -> i32 {
    let memBits: u8 = 30;
    let memPtr: *mut u8 = malloc(1 << memBits) as *mut u8;
    println!("Language: Rust (unsafe)");
    println!("Implementation: ");
    println!("Compiler: ");
    println!("Options: ");

    let mut i: u32 = 0; while i < 10 {
        testMem(memPtr, memBits);
        i += 1;
    }

    free(memPtr as *mut libc::c_void);
    return 0;
}

fn main() {
    unsafe { ::std::process::exit(main_0()) }
}
