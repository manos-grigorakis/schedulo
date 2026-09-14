"use client";

import MainInput from "@/components/forms/main-input";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import z from "zod";
import axios from "axios";
import { useRouter, useSearchParams } from "next/navigation";
import Link from "next/link";
import AuthContent from "../_components/auth-content";
import MainButton from "@/components/ui/main-button";

const schema = z.object({
  email: z.email({ message: "Please enter a valid email address" }),
  password: z.string().min(1, { message: "Password is required" }),
});

type LoginForm = z.infer<typeof schema>;

export default function Login() {
  const {
    register,
    handleSubmit,
    setError,
    formState: { errors },
  } = useForm<LoginForm>({
    resolver: zodResolver(schema),
  });
  const router = useRouter();
  const searchParams = useSearchParams();
  const oauthError = searchParams.get("error");

  const onSubmit = async (data: LoginForm) => {
    try {
      const res = await axios.post(
        `${process.env.NEXT_PUBLIC_BACKEND_API_URL}/auth/login`,
        data,
        { withCredentials: true },
      );

      if (res.status === 204) router.replace("/dashboard");
    } catch (error) {
      if (axios.isAxiosError(error) && error.response?.status === 401) {
        setError("root", { message: "Invalid credentials" });
        return;
      }

      setError("root", { message: "Something went wrong. Please try again" });
    }
  };

  return (
    <div>
      <AuthContent
        title="Welcome back"
        subtitle="Sign in to your shop dashboard"
      />

      <form
        onSubmit={handleSubmit(onSubmit)}
        className="flex flex-col space-y-4"
      >
        <MainInput
          id="email"
          label="Email"
          type="email"
          placeholder="giannis.papadopoulos@gmail.com"
          error={errors.email?.message}
          {...register("email")}
        />

        <MainInput
          id="password"
          label="Password"
          type="password"
          placeholder="••••••••"
          error={errors.password?.message}
          {...register("password")}
        />

        {errors.root?.message && (
          <div className="px-3 py-2 text-sm font-medium text-red-500 border border-red-200 rounded-lg bg-red-50">
            {errors.root.message}
          </div>
        )}

        {oauthError && (
          <div className="px-3 py-2 text-sm font-medium text-red-500 border border-red-200 rounded-lg bg-red-50">
            Something went wrong. Please try again
          </div>
        )}

        <MainButton title="Sign in" />

        <p className="text-center text-muted">
          New to Schedulo?{" "}
          <Link
            href="/register"
            className="font-medium text-primary-500 hover:underline"
          >
            Create One
          </Link>
        </p>
      </form>
    </div>
  );
}
