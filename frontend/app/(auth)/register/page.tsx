"use client";

import MainInput from "@/components/forms/main-input";
import AuthContent from "../_components/auth-content";
import z from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useRouter } from "next/navigation";
import MainButton from "@/components/ui/main-button";
import axios from "axios";
import Link from "next/link";

const schema = z.object({
  firstName: z
    .string()
    .trim()
    .min(1, { message: "First name is required" })
    .max(120, { message: "First name must be at most 120 characters" }),
  lastName: z
    .string()
    .trim()
    .min(1, { message: "Last name is required" })
    .max(120, { message: "Last name must be at most 120 characters" }),
  email: z.email({ message: "Please enter a valid email address" }),
  password: z
    .string()
    .min(8, "Password must be at least 8 characters")
    .max(20, "Password must be at most 20 characters")
    .regex(/[A-Z]/, "Password must contain an uppercase letter")
    .regex(/[a-z]/, "Password must contain an lowercase letter")
    .regex(/[0-9]/, "Password must contain a number")
    .regex(/[@#$%^&+=]/, "Password must contain a special character")
    .regex(/^\S+$/, "Password cannot contain spaces"),
  phone: z.string().optional(),
});

type RegisterForm = z.infer<typeof schema>;

export default function Register() {
  const {
    register,
    handleSubmit,
    setError,
    formState: { errors },
  } = useForm<RegisterForm>({
    resolver: zodResolver(schema),
  });
  const router = useRouter();

  const onSubmit = async (data: RegisterForm) => {
    try {
      const res = await axios.post(
        `${process.env.NEXT_PUBLIC_BACKEND_API_URL}/auth/registration`,
        data,
      );
      if (res.status === 201) router.replace("/dashboard");
    } catch (error) {
      setError("root", { message: "Something went wrong. Please try again" });
    }
  };

  return (
    <div>
      <AuthContent
        title="Create your account"
        subtitle="Get started with Schedulo"
      />

      <form
        onSubmit={handleSubmit(onSubmit)}
        className="flex flex-col space-y-4"
      >
        <div className="grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-2">
          <MainInput
            id="first-name"
            label="First Name"
            type="text"
            placeholder="Giannis"
            error={errors.firstName?.message}
            {...register("firstName")}
          />

          <MainInput
            id="last-name"
            label="Last Name"
            type="text"
            placeholder="Papadopoulos"
            error={errors.lastName?.message}
            {...register("lastName")}
          />
        </div>

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

        <MainInput
          id="phone"
          label="Phone"
          type="text"
          placeholder="123-456-7890"
          error={errors.phone?.message}
          {...register("phone")}
        />

        {errors.root?.message && (
          <div className="px-3 py-2 text-sm font-medium text-red-500 border border-red-200 rounded-lg bg-red-50">
            {errors.root.message}
          </div>
        )}

        <MainButton title="Create account" />

        <p className="text-center text-muted">
          Already have an account?{" "}
          <Link
            href="/login"
            className="font-medium text-primary-500 hover:underline"
          >
            Sign in
          </Link>
        </p>
      </form>
    </div>
  );
}
