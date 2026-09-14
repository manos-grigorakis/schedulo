import { InputHTMLAttributes } from "react";

interface MainInputProps extends InputHTMLAttributes<HTMLInputElement> {
  label: string;
  error?: string;
}

export default function MainInput({
  id,
  label,
  type = "text",
  placeholder,
  error,
  ...props
}: MainInputProps) {
  return (
    <div>
      <label htmlFor={id} className="block mb-1 font-medium">
        {label}
      </label>

      <input
        {...props}
        type={type}
        id={id}
        placeholder={placeholder}
        className="bg-white py-2 text-sm rounded-lg focus:outline-1 outline-primary-500 shadow-xs w-full px-2 border border-[#ccc]"
      />

      {error && <p className="mt-1 text-sm text-red-500">{error}</p>}
    </div>
  );
}
